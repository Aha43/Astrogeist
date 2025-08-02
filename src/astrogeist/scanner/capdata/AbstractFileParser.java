package astrogeist.scanner.capdata;

public abstract class AbstractFileParser implements FileParser {
	private String fileType;
	
	protected AbstractFileParser(String fileType) { this.fileType = fileType; }
	
	@Override
	public String getFileType() { return this.fileType; }
}
