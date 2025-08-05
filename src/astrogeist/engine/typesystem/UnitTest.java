package astrogeist.engine.typesystem;

import static astrogeist.engine.unittest.fluentassert.AssertThat.*;

public class UnitTest {
	public static void runAll() {
		System.out.println("--Testing typesystem--");
		exposureShouldResolveMsStrings();
		exposureShouldResolveSecondStrings();
		isAShouldWorkForExposure();
	}
	
	public static void exposureShouldResolveMsStrings() {
		exposureShouldResolveMsStrings_(
			"50ms",
			"100 ms",
			"10.35ms",
			"20.020 ms",
			"20.020 mS");}
	public static void exposureShouldResolveMsStrings_(String... strings) {
		for (var s : strings) {
			var resolvedType = Type.Exposure().resolve(s);
			that(resolvedType).isNotNull().isEqualTo(Type.ExposureInMilliseconds());
			System.out.println(s + " resolved to " + resolvedType);
		}
	}
	
	public static void exposureShouldResolveSecondStrings() {
		exposureShouldResolveSecondsStrings_(
			"50s",
			"100 s",
			"10.35s",
			"20.020 s",
			"12.5S");}
	public static void exposureShouldResolveSecondsStrings_(String... strings) {
		for (var s : strings) {
			var resolvedType = Type.Exposure().resolve(s);
			that(resolvedType).isNotNull().isEqualTo(Type.ExposureInSeconds());
			System.out.println(s + " resolved to " + resolvedType);
		}
	}
	
	public static void isAShouldWorkForExposure() {
		var result = Type.Exposure().isA(Type.Exposure());
		that(result).isTrue();
		System.out.println(Type.Exposure() + " is a " + Type.Exposure());
		
		result = Type.Exposure().isA(Type.ExposureInMilliseconds());
		that(result).isFalse();
		System.out.println(Type.Exposure() + " is not a " + Type.ExposureInMilliseconds());
		
		result = Type.Exposure().isA(Type.ExposureInSeconds());
		that(result).isFalse();
		System.out.println(Type.Exposure() + " is not a " + Type.ExposureInSeconds());
		
		result = Type.ExposureInMilliseconds().isA(Type.Exposure());
		that(result).isTrue();
		System.out.println(Type.ExposureInMilliseconds() + " is a " + Type.Exposure());
		
		result = Type.ExposureInSeconds().isA(Type.Exposure());
		that(result).isTrue();
		System.out.println(Type.ExposureInSeconds() + " is a " + Type.Exposure());
		
		result = Type.ExposureInMilliseconds().isA(Type.ExposureInMilliseconds());
		that(result).isTrue();
		System.out.println(Type.ExposureInMilliseconds() + " is a " + Type.ExposureInMilliseconds());
		
		result = Type.ExposureInSeconds().isA(Type.ExposureInSeconds());
		that(result).isTrue();
		System.out.println(Type.ExposureInSeconds() + " is a " + Type.ExposureInSeconds());
	}
	
	//public static void is

}
