package astrogeist.typesystem;

import static astrogeist.unittest.fluentassert.AssertThat.*;

public class UnitTest {
	public static void runAll() {
		System.out.println("--Testing typesystem--");
		ExposureShouldResolveMsStrings();
		ExposureShouldResolveSecondStrings();
	}
	
	public static void ExposureShouldResolveMsStrings() {
		ExposureShouldResolveMsStrings_(
			"50ms",
			"100 ms",
			"10.35ms",
			"20.020 ms");}
	public static void ExposureShouldResolveMsStrings_(String... strings) {
		for (var s : strings) {
			var resolvedType = Type.Exposure().resolve(s);
			that(resolvedType).isNotNull().isEqualTo(Type.ExposureInMilliseconds());
			System.out.println(s + " resolved to " + resolvedType);
		}
	}
	
	public static void ExposureShouldResolveSecondStrings() {
		ExposureShouldResolveSecondsStrings_(
			"50s",
			"100 s",
			"10.35s",
			"20.020 s");}
	public static void ExposureShouldResolveSecondsStrings_(String... strings) {
		for (var s : strings) {
			var resolvedType = Type.Exposure().resolve(s);
			that(resolvedType).isNotNull().isEqualTo(Type.ExposureInSeconds());
			System.out.println(s + " resolved to " + resolvedType);
		}
	}

}
