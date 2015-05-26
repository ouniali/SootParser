import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.util.Chain;

public class classDescription{
	
	public SootClass classe;
	public String name;
	public List<SootField> fields;
	public List<SootMethod> methods = new ArrayList<SootMethod>();
	public SootClass superClass;
	public SootClass subClass;
	public List<SootClass> superClasses;
	public List<SootClass> subClasses;
	public List<SootClass> attributRW;
	public HashMap<SootMethod, List<SootMethod>> callOut;
	public HashMap<SootMethod, List<SootMethod>> callIn;
	public List<SootClass> Assoc;
	public List<SootMethod> cohesion;
	
	public classDescription(SootClass classe ){
		
		this.classe=classe;
		this.name=classe.getName();
		this.fields=new ArrayList<SootField>();
		Chain ch=classe.getFields();
		fields.addAll(ch);		
		this.methods=classe.getMethods();
				
		this.superClasses=BuildData.superClasses(classe);
		this.subClasses=BuildData.subClasses(classe);
		
		this.Assoc=BuildData.getAssoc(this);	
				
		this.cohesion=BuildData.cohesion(this);
		this.attributRW=BuildData._attributRW(this);
		this.callOut=BuildData.callOut(this);
		this.callIn=BuildData.callIn(this);
	}	
}