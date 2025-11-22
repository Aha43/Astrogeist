package aha.common.util.unittest;

import static aha.common.util.Guards.throwStaticClassInstantiateError;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import aha.common.logging.Log;
import aha.common.util.AttributeObject;

import static aha.common.util.Strings.quote;

import static aha.common.unittest.fluentassert.AssertThat.*;

public final class AttributeObjectUnitTest {
	private AttributeObjectUnitTest() { throwStaticClassInstantiateError(); }
	
	private static Logger logger = Log.get(AttributeObject.class);
	
	public static void main(String[] args) { 
		Log.setGlobalLevel(Level.INFO);
		runAll(); 
	}
	
	public static void runAll() {
		setAndGetRoundTrip();
		getThrowsIfMissing();
		existsReflectsPresence();
		removeRemovesAndReturnsTrueIfPresent();
		setDoesNotAllowNullValues();
		getWithDefaultReturnsDefaultIfMissing();
		getWithDefaultReturnsStoredValueIfPresent();
		typedGetReturnsStoredValueIfAssignable();
		typedGetUsesStringConstructorWhenAvailable();
		typedGetWithDefaultUsesDefaultWhenMissing();
		getAsStringUsesToString();
		getAsBooleanSupportsBooleanAndString();
		getAsBooleanWithDefaultUsesDefaultWhenMissing();
		getAsCharSupportsCharacterAndSingleCharString();
		getAsShortSupportsShortAndString();
		getAsShortWithDefaultUsesDefaultWhenMissing();
		getAsCharThrowsIfStringHasLengthOtherThanOne();
		getAsCharWithDefaultUsesDefaultWhenMissing();
		getAsIntSupportsIntegerAndString();
		getAsLongSupportsLongAndString();
		getAsLongWithDefaultUsesDefaultWhenMissing();
		getAsFloatSupportsFloatAndString();
		getAsFloatWithDefaultUsesDefaultWhenMissing();
		getAsDoubleSupportsDoubleAndString();
		getAsBigDecimalSupportsBigDecimalAndString();
		asMapReturnsUnmodifiableCopy();
		fluentSetReturnsConcreteType();
		
		typeBasedSetAndGetRoundTrip();
		typeBasedGetUsesFullyQualifiedClassNameAsKey();
		typeBasedGetThrowsIfMissing();
		typeBasedExistsReflectsPresence();
		typeBasedRemoveRemovesAndReturnsTrueIfPresent();
		typeBasedSetOverwritesExistingOfSameType();
	}
	
	static void setAndGetRoundTrip() {
        var attrs = new AttributeObject().set("name", "Arne").set("age", 52);

        that(attrs.get("name")).isEqualTo("Arne");
        that(attrs.get("age")).isEqualTo(52);
        logger.info("setAndGetRoundTrip passed");
    }
	
	static void getThrowsIfMissing() {
		try {
			var attrs = new AttributeObject();
			attrs.get("missing");
		} catch (IllegalArgumentException x) {
			that(x.getMessage()).contains("missing");
			logger.info("getThrowsIfMissing passed");
			return;
		} catch (Exception x) {
        	throw new AssertionError(
        		"Expected 'IllegalArgumentException' not : " + 
        			quote(x.getClass().getName()));
        }
		
		throw new AssertionError("expected exception");
	}
	
	static void existsReflectsPresence() {
        var attrs = new AttributeObject();
        
        that(attrs.exists("key")).isFalse();
        attrs.set("key", "value");
        that(attrs.exists("key")).isTrue();
        logger.info("existsReflectsPresence passed");
    }
	
	static void removeRemovesAndReturnsTrueIfPresent() {
        var attrs = new AttributeObject().set("k1", "v1");

        that(attrs.exists("k1")).isTrue();
        that(attrs.remove("k1")).isTrue();
        that(attrs.exists("k1")).isFalse();

        // Removing again returns false
        that(attrs.remove("k1")).isFalse();
        
        logger.info("removeRemovesAndReturnsTrueIfPresent passed");
    }
	
	static void setDoesNotAllowNullValues() {
        var attrs = new AttributeObject();

        try {
        	attrs.set("nullValue", null);
        }
        catch (NullPointerException np) {
        	logger.info("setDoesNotAllowNullValues passed");
        	return;
        }
        catch (Exception x) {
        	throw new AssertionError("Expected 'NullPointerException' not : " + 
        		quote(x.getClass().getName()));
        }
        
        throw new AssertionError("expected exception");
    }
	
	static void getWithDefaultReturnsDefaultIfMissing() {
        var attrs = new AttributeObject();

        that(attrs.get("missing", "default")).isEqualTo("default");
        logger.info("getWithDefaultReturnsDefaultIfMissing passed");
    }
	
	static void getWithDefaultReturnsStoredValueIfPresent() {
        var attrs = new AttributeObject().set("city", "Bergen");

        that(attrs.get("city", "default")).isEqualTo("Bergen");
        logger.info("getWithDefaultReturnsStoredValueIfPresent passed");
    }
	
	static void typedGetReturnsStoredValueIfAssignable() {
        var attrs = new AttributeObject().set("count", 123);

        Integer count = attrs.get("count", Integer.class);
        that(count).isNotNull().isEqualTo(123);
        logger.info("typedGetReturnsStoredValueIfAssignable passed");
    }
	
	static void typedGetUsesStringConstructorWhenAvailable() {
        var attrs = new AttributeObject().set("amount", "42");

        Amount a = attrs.get("amount", Amount.class);
        that(a.value()).isEqualTo(42);
        logger.info("typedGetUsesStringConstructorWhenAvailable passed");
    }
	
	static void typedGetWithDefaultUsesDefaultWhenMissing() {
        var attrs = new AttributeObject();

        Integer value = attrs.get("count", Integer.class, 99);
        that(value).isEqualTo(99);
        logger.info("typedGetWithDefaultUsesDefaultWhenMissing passed");
    }
 
	static void getAsStringUsesToString() {
        var attrs = new AttributeObject().set("num", 123);

        that(attrs.getAsString("num")).isEqualTo("123");
        logger.info("getAsStringUsesToString passed");
    }
   
	static void getAsBooleanSupportsBooleanAndString() {
        var attrs = new AttributeObject().set("b1", true).set("b2", "false");

        Boolean b1 = attrs.getAsBoolean("b1");
        Boolean b2 = attrs.getAsBoolean("b2");
        that(b1).isNotNull().isEqualTo(true);
        that(b2).isNotNull().isEqualTo(false);
        logger.info("getAsBooleanSupportsBooleanAndString passed");
    }
    
	static void getAsBooleanWithDefaultUsesDefaultWhenMissing() {
        var attrs = new AttributeObject();

        Boolean missingTrue = attrs.getAsBoolean("missingTrue", true);
        Boolean missingFalse = attrs.getAsBoolean("missingFalse", false);
        that(missingTrue).isNotNull().isEqualTo(true);
        that(missingFalse).isNotNull().isEqualTo(false);
        logger.info("getAsBooleanWithDefaultUsesDefaultWhenMissing passed");
    }
    
	static void getAsCharSupportsCharacterAndSingleCharString() {
        var attrs = new AttributeObject().set("c1", 'A').set("c2", "Z");

        that(attrs.getAsChar("c1")).isEqualTo('A');
        that(attrs.getAsChar("c2")).isEqualTo('Z');
        logger.info("getAsCharSupportsCharacterAndSingleCharString passed");
    }

	static void getAsCharWithDefaultUsesDefaultWhenMissing() {
        var attrs = new AttributeObject();

        that(attrs.getAsChar("missing", 'X')).isEqualTo('X');
        logger.info("getAsCharWithDefaultUsesDefaultWhenMissing passed");
    }

	static void getAsCharThrowsIfStringHasLengthOtherThanOne() {
        var attrs = new AttributeObject().set("badChar", "AB");

        try {
        	attrs.getAsChar("badChar");
        } catch (IllegalArgumentException iax) {
        	that(iax.getMessage()).contains("badChar");
        	logger.info("getAsCharWithDefaultUsesDefaultWhenMissing passed");
        	return;
        } catch (Exception x) {
        	throw new AssertionError(
        		"Expected 'IllegalArgumentException' not : " + 
            		quote(x.getClass().getName()));
        }
        
        throw new AssertionError("expected exception");
    }

	static void getAsShortSupportsShortAndString() {
        var attrs = new AttributeObject().set("s1", (short)12).set("s2", "34");

        that(attrs.getAsShort("s1")).isEqualTo(12);
        that(attrs.getAsShort("s2")).isEqualTo(34);
        logger.info("getAsShortSupportsShortAndString passed");
    }

	static void getAsShortWithDefaultUsesDefaultWhenMissing() {
        var attrs = new AttributeObject();

        that(attrs.getAsShort("missing", (short)7)).isEqualTo(7);
        logger.info("getAsShortWithDefaultUsesDefaultWhenMissing passed");
    }
  
	static void getAsIntSupportsIntegerAndString() {
        var attrs = new AttributeObject().set("i1", 123).set("i2", "456");

        that(attrs.getAsInt("i1")).isEqualTo(123);
        that(attrs.getAsInt("i2")).isEqualTo(456);
        logger.info("getAsIntSupportsIntegerAndString passed");
    }
 
	static void getAsIntWithDefaultUsesDefaultWhenMissing() {
        var attrs = new AttributeObject();

        that(attrs.getAsInt("missing", 10)).isEqualTo(10);
        logger.info("getAsIntWithDefaultUsesDefaultWhenMissing passed");
    }
    
	static void getAsLongSupportsLongAndString() {
        var attrs = new AttributeObject().set("l1", 123L).set("l2", "456");
        
        that(attrs.getAsLong("l1")).isEqualTo(123L);
        that(attrs.getAsLong("l2")).isEqualTo(456L);
        logger.info("getAsLongSupportsLongAndString passed");
	}

	static void getAsLongWithDefaultUsesDefaultWhenMissing() {
        var attrs = new AttributeObject();

        that(attrs.getAsLong("missing", 999L)).isEqualTo(999L);
        logger.info("getAsLongWithDefaultUsesDefaultWhenMissing passed");
    }
 
    static void getAsFloatSupportsFloatAndString() {
        var attrs = new AttributeObject().set("f1", 1.5f).set("f2", "2.5");

        that(attrs.getAsFloat("f1")).isEqualTo(1.5f);
        that(attrs.getAsFloat("f2")).isEqualTo(2.5f);
        logger.info("getAsFloatSupportsFloatAndString passed");
    }

    static void getAsFloatWithDefaultUsesDefaultWhenMissing() {
        var attrs = new AttributeObject();

        that(attrs.getAsFloat("missing", 3.14f)).isEqualTo(3.14f);
        logger.info("getAsFloatWithDefaultUsesDefaultWhenMissing passed");
    }
   
    static void getAsDoubleSupportsDoubleAndString() {
        var attrs = new AttributeObject().set("d1", 1.5d).set("d2", "2.5");

        that(attrs.getAsDouble("d1")).isEqualTo(1.5d);
        that(attrs.getAsDouble("d2")).isEqualTo(2.5d);
        logger.info("getAsDoubleSupportsDoubleAndString passed");
    }
   
    static void getAsBigDecimalSupportsBigDecimalAndString() {
        var attrs = new AttributeObject().set("bd1", new BigDecimal("12.34"))
        	.set("bd2", "56.78");

        that(attrs.getAsBigDecimal("bd1")).isEqualTo(new BigDecimal("12.34"));
        that(attrs.getAsBigDecimal("bd2")).isEqualTo(new BigDecimal("56.78"));
        logger.info("getAsBigDecimalSupportsBigDecimalAndString passed");
    }
    
    static void asMapReturnsUnmodifiableCopy() {
        var attrs = new AttributeObject().set("a", 1).set("b", 2);

        var map = attrs.asMap();

        that(map).isNotNull();
        that(map.size()).isEqualTo(2);
        that(map.get("a")).isEqualTo(1);
       
        try {
        	map.put("c", 3);
        } catch (UnsupportedOperationException uox) {
        	logger.info("asMapReturnsUnmodifiableCopy passed");
        	return;
        }
        catch (Exception x) {
        	throw new AssertionError(
        		"Expected 'UnsupportedOperationException' not : " + 
            		quote(x.getClass().getName()));
        }

        throw new AssertionError("expected exception");
    }
    
    static void fluentSetReturnsConcreteType() {
        var attrs = new AttributeObject().set("x", 1).set("y", 2);

        // If this compiles and attrs is AttributeObject, the self-typing works.
        that(attrs).isNotNull();
        that(attrs.getAsInt("x")).isEqualTo(1);
        that(attrs.getAsInt("y")).isEqualTo(2);
        logger.info("fluentSetReturnsConcreteType passed");
    }
    
    
    static final class FooContext {
        final String value;
        FooContext(String value) { this.value = value; }
    }

    static final class BarContext {
        final int number;
        BarContext(int number) { this.number = number; }
    }

    static void typeBasedSetAndGetRoundTrip() {
        var attrs = new AttributeObject();

        var foo = new FooContext("hello");
        var bar = new BarContext(42);

        attrs.set(foo).set(bar);

        var fooResult = attrs.get(FooContext.class);
        var barResult = attrs.get(BarContext.class);

        that(fooResult).isEqualTo(foo);
        that(barResult).isEqualTo(bar);
        that(fooResult.value).isEqualTo("hello");
        that(barResult.number).isEqualTo(42);
        logger.info("typeBasedSetAndGetRoundTrip passed");
    }

    static void typeBasedGetUsesFullyQualifiedClassNameAsKey() {
        var attrs = new AttributeObject();
        FooContext foo = new FooContext("x");

        attrs.set(foo);

        // asMap should contain the FQN key
        var map = attrs.asMap();
        that(map.containsKey(FooContext.class.getName())).isTrue();
        that(map.get(FooContext.class.getName())).isEqualTo(foo);
        logger.info("typeBasedGetUsesFullyQualifiedClassNameAsKey passed");
    }

    static void typeBasedGetThrowsIfMissing() {
        var attrs = new AttributeObject();

        try {
        	attrs.get(FooContext.class);
        } catch (IllegalArgumentException iax) {
        	logger.info("typeBasedGetThrowsIfMissing passed");
        	return;
        } catch (Exception x) {
        	throw new AssertionError(
        		"Expected 'IllegalArgumentException' not : " + 
        			quote(x.getClass().getName()));
        }
        
        throw new AssertionError("expected exception");
    }
    
    static void typeBasedExistsReflectsPresence() {
        var attrs = new AttributeObject();

        that(attrs.exists(FooContext.class)).isFalse();
        attrs.set(new FooContext("abc"));
        that(attrs.exists(FooContext.class)).isTrue();
        logger.info("typeBasedExistsReflectsPresence passed");
    }
    
    static void typeBasedRemoveRemovesAndReturnsTrueIfPresent() {
        var attrs = new AttributeObject().set(new FooContext("abc"));

        that(attrs.exists(FooContext.class)).isTrue();
        that(attrs.remove(FooContext.class)).isTrue();
        that(attrs.exists(FooContext.class)).isFalse();
        that(attrs.remove(FooContext.class)).isFalse();
        logger.info("typeBasedRemoveRemovesAndReturnsTrueIfPresent passed");
    }
    
    static void typeBasedSetOverwritesExistingOfSameType() {
        var attrs = new AttributeObject().set(new FooContext("first"));

        attrs.set(new FooContext("second"));

        var result = attrs.get(FooContext.class);
        that(result.value).isEqualTo("second");
        logger.info("typeBasedSetOverwritesExistingOfSameType passed");
    }    

}
