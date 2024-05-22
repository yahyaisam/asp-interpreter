package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.*;
import java.lang.Math;


public class RuntimeFloatValue extends RuntimeValue {
    float floatValue;

    public RuntimeFloatValue(double n) {
        floatValue = (float) n;
    }

	@Override
	public String getStringValue(String what, AspSyntax where) {
		return floatValue + "";
	}

    @Override
    public String typeName() {
        return "float";
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return (long) floatValue;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return floatValue;
    }

	@Override
    public boolean getBoolValue(String what, AspSyntax where) {
		return (this.floatValue != 0.0);
    }

	@Override
	public String toString() {
		return "" + floatValue;
	}

    @Override
	public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {

		double fv;

		switch (v.typeName()) {

            case ("float"):
				fv = this.floatValue + v.getFloatValue("float", where);
                break;

			case ("int"):
				fv = this.floatValue + v.getIntValue("int", where);
                break;

            default:
				runtimeError("'+' undefined for "+v.typeName()+"!", where);
				return null;
        	}
			
			return new RuntimeFloatValue(fv);
		}

	@Override
	public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {

		double fv;
		

		switch (v.typeName()) {
			case ("float"):
				fv = this.floatValue / v.getFloatValue("float", where);
				break;

			case ("int"):
				fv = this.floatValue / v.getIntValue("int", where);
				break;

			default:
				runtimeError("'/' undefined for "+v.typeName()+"!", where);
				return null;
			}
			
			return new RuntimeFloatValue(fv);
		}

	@Override
	public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {

		boolean fb;

		switch (v.typeName()) {
			
			case ("float"):

				if (this.floatValue == v.getFloatValue("float", where)) fb = true;
				else fb = false;
				break;

			case ("int"):
				if (this.floatValue == v.getIntValue("int", where)) fb = true;
				else fb = false;
				break;

			default:
				runtimeError("'==' undefined for "+v.typeName()+"!", where);
				return null;
			}
			
			return new RuntimeBoolValue(fb);
	}

	@Override
	public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
		
		boolean fb;

		switch (v.typeName()) {
			
			case ("float"):

				if (this.floatValue > v.getFloatValue("float", where)) fb = true;
				else fb = false;
				break;

			case ("int"):
				if (this.floatValue > v.getIntValue("int", where)) fb = true;
				else fb = false;
				break;

			default:
				runtimeError("'>' undefined for "+v.typeName()+"!", where);
				return null;
			}
			
			return new RuntimeBoolValue(fb);
	}

	@Override
	public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
		
		boolean fb;

		switch (v.typeName()) {
			
			case ("float"):

				if (this.floatValue >= v.getFloatValue("float", where)) fb = true;
				else fb = false;
				break;

			case ("int"):
				if (this.floatValue >= v.getIntValue("int", where)) fb = true;
				else fb = false;
				break;

			default:
				runtimeError("'>=' undefined for "+v.typeName()+"!", where);
				return null;
			}
			
			return new RuntimeBoolValue(fb);
	}

	@Override
	public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
		
		int fv;

		switch (v.typeName()) {

			case ("float"):
				fv = (int) Math.floor(this.floatValue / v.getFloatValue("float", where));
				break;

			case ("int"):
				fv = (int) Math.floor(this.floatValue / v.getFloatValue("int", where));
				break;

			default:
				runtimeError("'//' undefined for "+v.typeName()+"!", where);
				return null;
			}
			
			return new RuntimeIntValue(fv);
	}
	
	@Override
	public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
		
		boolean fb;

		switch (v.typeName()) {
			
			case ("float"):

				if (this.floatValue < v.getFloatValue("float", where)) fb = true;
				else fb = false;
				break;

			case ("int"):
				if (this.floatValue < v.getIntValue("int", where)) fb = true;
				else fb = false;
				break;

			default:
				runtimeError("'<' undefined for "+v.typeName()+"!", where);
				return null;
			}
			
			return new RuntimeBoolValue(fb);
	}
	
	@Override
	public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {

		boolean fb;

		switch (v.typeName()) {
			
			case ("float"):

				if (this.floatValue <= v.getFloatValue("float", where)) fb = true;
				else fb = false;
				break;

			case ("int"):
				if (this.floatValue <= v.getIntValue("int", where)) fb = true;
				else fb = false;
				break;

			default:
				runtimeError("'<=' undefined for "+v.typeName()+"!", where);
				return null;
			}
			
			return new RuntimeBoolValue(fb);
	}

	@Override
	public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {

		double fd;

		switch (v.typeName()) {
			
			case ("float"):
				fd = this.floatValue - v.getFloatValue("float", where) * Math.floor(this.floatValue / v.getFloatValue("float", where));
				break;

			case ("int"):
				fd = this.floatValue - v.getFloatValue("int", where) * Math.floor(this.floatValue / v.getFloatValue("int", where));
				break;

			default:
				runtimeError("'%' undefined for "+v.typeName()+"!", where);
				return null;
			}
			
			return new RuntimeFloatValue(fd);
	}
	
	@Override
	public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
		
	double fv;

	switch (v.typeName()) {

		case ("float"):
			fv = this.floatValue * v.getFloatValue("float", where);
			break;

		case ("int"):
			fv = this.floatValue * v.getIntValue("int", where);
			break;

		default:
			runtimeError("'*' undefined for " + v.typeName()+"!", where);
			return null;
		}
		
		return new RuntimeFloatValue(fv);
	}

	@Override
	public RuntimeValue evalNegate(AspSyntax where) {
		return new RuntimeFloatValue(-floatValue);
	}

	@Override
	public RuntimeValue evalNot(AspSyntax where) {
		return new RuntimeBoolValue(!this.getBoolValue("float", where));
	}

	@Override
	public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
		boolean fb;

		if (this.floatValue != v.getFloatValue("float", where)) fb = true;
		else fb = false;

		return new RuntimeBoolValue(fb);
	}

	@Override
	public RuntimeValue evalPositive(AspSyntax where) {
		if (this.floatValue >= 0) {
			return new RuntimeBoolValue(true);
		}

		return new RuntimeBoolValue(false);
	}

	@Override
	public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
		double fv;

		switch (v.typeName()) {
			case ("float"):
				fv = this.floatValue - v.getFloatValue("float", where);
				break;

			case ("int"):
				fv = this.floatValue - v.getIntValue("int", where);
				break;

			default:
				runtimeError("'-' undefined for "+v.typeName()+"!", where);
				return null;
			}
			
			return new RuntimeFloatValue(fv);
	}

}
