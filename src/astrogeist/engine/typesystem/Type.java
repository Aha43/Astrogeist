package astrogeist.engine.typesystem;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Stack;
import java.util.regex.Pattern;

import astrogeist.engine.util.FilesUtil;

public abstract class Type {
	
	public final String getName() { return this.getClass().getSimpleName(); }
	
	public Type getParentType() { return null; }
	
	public String getUnit() { return ""; }
	
	protected Type() {}
	
	@Override public String toString() { return getName(); }
	
	public boolean isA(Type type) 
	{ 
		if (type == null) return false;
		return type.getClass().isAssignableFrom(this.getClass());
	}
	
	public static boolean isA(Type a, Type b) {
		if (a == null) return false;
		if (b == null) return false;
		return a.isA(b);
	}
	
	private String breadcrumbs = null;
	
	public final String toBreadcrumbString() {
		if (this.breadcrumbs != null) return this.breadcrumbs;
		
		var stack = new Stack<String>();
		var curr = this;
		while (curr != null) { stack.push(curr.getName()); curr = curr.getParentType(); }
		var sb = new StringBuilder();
		while (!stack.isEmpty()) {
			if (sb.length() > 0) sb.append(" <- ");
			var name = stack.pop();
			sb.append(name);
		}
		this.breadcrumbs = sb.toString();
		return this.breadcrumbs;
	}
	
	@Override final public boolean equals(Object o) { return this == o; }
	@Override final public int hashCode() { return System.identityHashCode(this); }
	
	// TYPES
	
	private static final Void VOID_INSTANCE = new Void();
	public static final Void Void() { return VOID_INSTANCE; }
	public static final class Void extends Type { private Void() {} }
	
	private static final Text TEXT_INSTANCE = new Text();
	public static Text Text() { return TEXT_INSTANCE; }
	public static final class Text extends Type {
		protected Text() {}
		public final Text resolve(String s) {
			return s == null ? null : (s.trim().length() == 0 ? null : this); }
	}
	
	private static final Number NUMBER_INSTANCE = new Number();
	public static Number Number() { return NUMBER_INSTANCE; }
	public static class Number extends Type { 
		protected Number() {}
		
		public boolean canBeZero() { return true; }
		public boolean canBeNegative() { return true; }
		public boolean canBePositive() { return true; }
		
		public final boolean mustBeNonZero() { return !canBeZero(); }
		public final boolean mustBePositive() { return !canBeNegative(); }
		public final boolean mustBeNegative() { return !canBePositive(); }
		
		public double getMin() { return Double.NEGATIVE_INFINITY; }
	    public double getMax() { return Double.POSITIVE_INFINITY; }
		
		public void validate(double n) throws TypeValidationException {
			if (this.mustBeNonZero() && n == 0)
				throw new TypeValidationException(this, n, "can not be zero"); 
			
			if (this.mustBePositive() && n < 0)
				throw new TypeValidationException(this, n, "can not be less than zero"); 
			
			if (this.mustBeNegative() && n > 0)
				throw new TypeValidationException(this, n, "can not be larger than zero");
			
			if (n < getMin())
	            throw new TypeValidationException(this, n, "must be >= " + getMin());

	        if (n > getMax())
	            throw new TypeValidationException(this, n, "must be <= " + getMax());
		}
	}
	
	private static final Decimal DECIMAL_INSTANCE = new Decimal();
	public static Decimal Decimal() { return DECIMAL_INSTANCE; }
	public static class Decimal extends Number {
		protected Decimal() {}
		@Override public Type getParentType() { return Type.Number(); }
		
	}
	
	private static final Integer INTEGER_INSTANCE = new Integer();
	public static Integer Integer() { return INTEGER_INSTANCE; }
	public static class Integer extends Number {
		protected Integer() {}
		@Override public Type getParentType() { return Type.Number(); }
	}
	
	private static final FrameCount FRAMECOUNT_INSTANCE = new FrameCount();
	public static FrameCount FrameCount() { return FRAMECOUNT_INSTANCE; }
	public static final class FrameCount extends Integer {
		private FrameCount() {}
		@Override public Type getParentType() { return Type.Integer(); }
		@Override public final boolean canBeZero() { return false; }
		@Override public final boolean canBeNegative() { return false; }
	}
	
	private static final Exposure EXPOSURE_INSTANCE = new Exposure();
	public static Exposure Exposure() { return EXPOSURE_INSTANCE; }
	public static class Exposure extends Decimal {
		protected Exposure() {}
		@Override public Type getParentType() { return Type.Decimal(); }
		@Override public final boolean canBeZero() { return false; }
		@Override public final boolean canBeNegative() { return false; }
		public Exposure resolve(String s) {
			if (s == null || s.trim().length() == 0) return null;
			var parsed = parseNumberWithSuffix(s);
			if (parsed.isEmpty()) return null;
			var suffix = parsed.get().suffix().toLowerCase();
			return (suffix.equals("ms") ? Type.ExposureInMilliseconds() : 
				(suffix.equals("s") ? Type.ExposureInSeconds() : null));
		}
	}
	
	private static final ExposureInMilliseconds EXPOSUREINMILLISECONDS_INSTANCE = new ExposureInMilliseconds();
	public static ExposureInMilliseconds ExposureInMilliseconds() { return EXPOSUREINMILLISECONDS_INSTANCE; }
	public static final class ExposureInMilliseconds extends Exposure {
		private ExposureInMilliseconds() { }
		@Override public Type getParentType() { return Type.Exposure(); }
		@Override public final String getUnit() { return "ms"; }
	}
	
	private static final ExposureInSeconds EXPOSUREINSECONDS_INSTANCE = new ExposureInSeconds();
	public static ExposureInSeconds ExposureInSeconds() { return EXPOSUREINSECONDS_INSTANCE; }
	public static final class ExposureInSeconds extends Exposure {
		private ExposureInSeconds() { }
		@Override public Type getParentType() { return Type.Exposure(); }
		@Override public final String getUnit() { return "s"; }
	}
	
	private static final Gain GAIN_INSTANCE = new Gain();
	public static final Gain Gain() { return GAIN_INSTANCE; } 
	public static final class Gain extends Integer {
		private Gain() {}
		@Override public Type getParentType() { return Type.Integer(); }
		@Override public boolean canBeNegative() { return false; }
	}
	
	private static final DiskFile DISKFILE_INSTANCE = new DiskFile();
	public static final DiskFile DiskFile() { return DISKFILE_INSTANCE; } 
	public static class DiskFile extends Type {
		protected DiskFile() {}
		
		public final DiskFile resolve(String path) {
			return (path == null) ? null : resolve(Path.of(path)); }
		public final DiskFile resolve(File file) { 
			return (file == null) ? null : resolve(file.toPath()); }
		public final DiskFile resolve(Path path) {
			if (path == null) return null;
			var ext = FilesUtil.getExtension(path);
			if (ext.length() == 0) return Type.DiskFile();
			switch (ext.toLowerCase()) {
				case "txt"  : return Type.TxtFile();
				case "ser"  : return Type.SerFile();
				case "fits" : return Type.FitsFile();
				case "tif"  : return Type.TifFile();
				case "jpg"  : return Type.JpgFile();
				case "png"  : return Type.PngFile();
				case "gif"  : return Type.GifFile();
				default : return Type.DiskFile();
			}
		}
		
		public String getFileTypeName() { return "UNKNOWN"; }
	}
	
	private static final TxtFile TXTFILE_INSTANCE = new TxtFile();
	public static final TxtFile TxtFile() { return TXTFILE_INSTANCE; } 
	public static final class TxtFile extends DiskFile {
		private TxtFile() {}
		@Override public Type getParentType() { return Type.DiskFile(); }
		@Override public String getFileTypeName() { return "TXT"; }
	}
	
	private static final SerFile SERFILE_INSTANCE = new SerFile();
	public static final SerFile SerFile() { return SERFILE_INSTANCE; }
	public static final class SerFile extends DiskFile {
		private SerFile() {}
		@Override public Type getParentType() { return Type.DiskFile(); }
		@Override public String getFileTypeName() { return "SER"; }
	}
	
	private static final FitsFile FITSFILE_INSTANCE = new FitsFile();
	public static final FitsFile FitsFile() { return FITSFILE_INSTANCE; }
	public static final class FitsFile extends DiskFile {
		private FitsFile() {}
		@Override public Type getParentType() { return Type.DiskFile(); }
		@Override public String getFileTypeName() { return "FIT"; }
	}
	
	private static final TifFile TIFFILE_INSTANCE = new TifFile();
	public static final TifFile TifFile() { return TIFFILE_INSTANCE; }
	public static final class TifFile extends DiskFile {
		private TifFile() {}
		@Override public Type getParentType() { return Type.DiskFile(); }
		@Override public String getFileTypeName() { return "TIF"; }
	}
	
	private static final JpgFile JPGFILE_INSTANCE = new JpgFile();
	public static final JpgFile JpgFile() { return JPGFILE_INSTANCE; }
	public static final class JpgFile extends DiskFile {
		private JpgFile() {}
		@Override public Type getParentType() { return Type.DiskFile(); }
		@Override public String getFileTypeName() { return "JPG"; }
	}
	
	private static final PngFile PNGFILE_INSTANCE = new PngFile();
	public static final PngFile PngFile() { return PNGFILE_INSTANCE; }
	public static final class PngFile extends DiskFile {
		private PngFile() {}	
		@Override public Type getParentType() { return Type.DiskFile(); }
		@Override public String getFileTypeName() { return "PNG"; }
	}
	
	private static final GifFile GIFFILE_INSTANCE = new GifFile();
	public static final GifFile GifFile() { return GIFFILE_INSTANCE; }
	public static final class GifFile extends DiskFile {
		private GifFile() {}
		@Override public Type getParentType() { return Type.DiskFile(); }
		@Override public String getFileTypeName() { return "GIF"; }
	}
	
	public static Optional<ParsedValue> parseNumberWithSuffix(String input) {
	    var pattern = Pattern.compile("^\\s*([+-]?\\d+(\\.\\d+)?)\\s*(.*)$");
	    var matcher = pattern.matcher(input);

	    if (matcher.matches()) {
	        double number = Double.parseDouble(matcher.group(1));
	        String suffix = matcher.group(3).trim();
	        return Optional.of(new ParsedValue(number, suffix));
	    }

	    return Optional.empty(); // not a valid number at the beginning
	}
	
}
