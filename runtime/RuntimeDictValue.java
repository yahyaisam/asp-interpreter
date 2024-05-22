package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.*;
import java.util.HashMap;
import java.util.Map;

public class RuntimeDictValue extends RuntimeValue {

	// Vi bruker hashmap til aa lagre verdiene og sammenligner runtime-objektene med equal!
	// Alle instanser av Runtime har dette!
	HashMap<RuntimeValue, RuntimeValue> dict;

    public RuntimeDictValue(HashMap<RuntimeValue, RuntimeValue> add) {
        dict = add;
    }

    @Override
    public String typeName() {
        return "dict";
    }

	@Override
    public String toString() {
		String s = "{";
		int count = 0;
	
		for (Map.Entry<RuntimeValue, RuntimeValue> set : dict.entrySet()) {
			String key = set.getKey().toString();
			String value = set.getValue().toString();
			if (set.getKey() instanceof RuntimeStringValue) key = set.getKey().showInfo();
			if (set.getValue() instanceof RuntimeStringValue) value = set.getValue().showInfo();
			s += key + ": " + value;
			if (count++ != dict.size()-1) s += ", ";
		}
		s += "}";
		return s;
    }

	@Override
    public boolean getBoolValue(String what, AspSyntax where) {
		return (dict.size() != 0);
    }

	@Override
	public RuntimeValue evalLen(AspSyntax where) {
	    return new RuntimeIntValue(this.dict.size());
	}

	@Override
	public RuntimeValue evalNot(AspSyntax where) {
		return new RuntimeBoolValue(!this.getBoolValue("dict", where));
	}

	@Override
	public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {

		if (v.typeName().equals("none")) {
			return new RuntimeBoolValue(this.getBoolValue("dict", where) != v.getBoolValue("none", where));
		}

		else {
			runtimeError("!=' undefined for " + v.typeName() + "!", where);
			return null; 

		}
	}

	@Override
	public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {

		if (v.typeName().equals("none")) {
			return new RuntimeBoolValue(this.getBoolValue("dict", where) == v.getBoolValue("none", where));
		}

		else {
			runtimeError("!=' undefined for " + v.typeName() + "!", where);
			return null; 
		}
	}

	@Override
	public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
		

		if (v.typeName().equals("string")) {

			RuntimeValue nokkel = null;
			for (RuntimeValue d : dict.keySet()) {
				if (d.toString().equals(v.toString())) nokkel = d;
			}
			return dict.get(nokkel);
		}

		else {
			runtimeError("'{...}' undefined for " + v.typeName() + "!", where);
			return null;
		}
		
	}
}
