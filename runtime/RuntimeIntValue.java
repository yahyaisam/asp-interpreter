package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.*;

public class RuntimeIntValue extends RuntimeValue {
    long intValue;

    public RuntimeIntValue(long n) {
        intValue = n;
    }

    @Override
    public String typeName() {
        return "int";
    }

	@Override
	public String getStringValue(String what, AspSyntax where) {
		return intValue + "";
	}

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return intValue;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return intValue;
    }

	@Override
    public boolean getBoolValue(String what, AspSyntax where) {
		return (this.intValue != 0);
    }

	@Override
	public String toString() {
		return "" + intValue;
	}

    @Override
	public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {


		switch (v.typeName()) {

            case ("float"):
				double fv = this.intValue + v.getFloatValue("float", where);
                return new RuntimeFloatValue(fv);
			

			case ("int"):
				long fl = this.intValue + v.getIntValue("int", where);
                return new RuntimeIntValue(fl);
				

            default:
				runtimeError("'+' undefined for "+v.typeName()+"!", where);
				return null;
        	}
		}

	@Override
	public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {

		double fv;

		switch (v.typeName()) {
			case ("float"):
				fv = this.intValue / v.getFloatValue("float", where);
				break;

			case ("int"):
				fv = (double) this.intValue / v.getIntValue("int", where);
				break;

			default:
				runtimeError("'/' undefined for "+v.typeName()+"!", where);
				return null;
			}
			if(fv == (long) fv) return new RuntimeIntValue((long) fv);
			
			return new RuntimeFloatValue(fv);
		}

	@Override
	public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {

		boolean fb;

		switch (v.typeName()) {
			
			case ("float"):

				if (this.intValue == v.getFloatValue("float", where)) fb = true;
				else fb = false;
				break;

			case ("int"):
				if (this.intValue == v.getIntValue("int", where)) fb = true;
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

				if (this.intValue > v.getFloatValue("float", where)) fb = true;
				else fb = false;
				break;

			case ("int"):
				if (this.intValue > v.getIntValue("int", where)) fb = true;
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

				if (this.intValue >= v.getFloatValue("float", where)) fb = true;
				else fb = false;
				break;

			case ("int"):
				if (this.intValue >= v.getIntValue("int", where)) fb = true;
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
				fv = (int) Math.floor(this.intValue / v.getFloatValue("float", where));
				break;

			case ("int"):
				fv = (int) Math.floorDiv(this.intValue, v.getIntValue("int", where));
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

				if (this.intValue < v.getFloatValue("float", where)) fb = true;
				else fb = false;
				break;

			case ("int"):
				if (this.intValue < v.getIntValue("int", where)) fb = true;
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

				if (this.intValue <= v.getFloatValue("float", where)) fb = true;
				else fb = false;
				break;

			case ("int"):
				if (this.intValue <= v.getIntValue("int", where)) fb = true;
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
				fd = this.intValue - v.getFloatValue("float", where) * Math.floor(this.intValue / v.getFloatValue("float", where));
				break;

			case ("int"):
				fd = Math.floorMod(this.intValue, v.getIntValue("int", where));
				break;

			default:
				runtimeError("'%' undefined for "+v.typeName()+"!", where);
				return null;
			}
			if(fd == (long) fd) return new RuntimeIntValue((long) fd);
			
			return new RuntimeFloatValue(fd);
	}
	
	@Override
	public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
		
		double fv;

		if (v.typeName().equals("float")) {
			fv = intValue * v.getFloatValue("* operator", where);

			if(fv == (long) fv) return new RuntimeIntValue((long) fv);

			return new RuntimeFloatValue(fv);
			
		}

		if (v.typeName().equals("int")) {
			return new RuntimeIntValue(intValue * v.getIntValue("* operator", where));
		}

		else {
			runtimeError("'*' undefined for "+v.typeName()+"!", where);
			return null;
		}
	}

	@Override
	public RuntimeValue evalNegate(AspSyntax where) {
	
		return new RuntimeIntValue(-this.intValue);
	}

	@Override
	public RuntimeValue evalNot(AspSyntax where) {
		return new RuntimeBoolValue(!this.getBoolValue("int", where));
	}

	@Override
	public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
		boolean fb;

		if (this.intValue != v.getIntValue("int", where)) fb = true;
		else fb = false;

		return new RuntimeBoolValue(fb);
	}

	@Override
	public RuntimeValue evalPositive(AspSyntax where) {
		if (this.intValue >= 0) {
			return new RuntimeBoolValue(true);
		}

		return new RuntimeBoolValue(false);
	}

	@Override
	public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
		double fv;

		switch (v.typeName()) {
			case ("float"):
				fv = this.intValue - v.getFloatValue("float", where);
				break;

			case ("int"):
				fv = this.intValue - v.getIntValue("int", where);
				return new RuntimeIntValue((int) fv);
				

			default:
				runtimeError("'-' undefined for "+v.typeName()+"!", where);
				return null;
			}
		if(fv == (long) fv) return new RuntimeIntValue((long) fv);
		return new RuntimeFloatValue(fv);
	}
}
