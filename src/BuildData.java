import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import soot.*;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.CallGraphBuilder;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.OneCFAContextManager;
import soot.jimple.toolkits.callgraph.Targets;
import soot.options.Options;
import soot.util.queue.QueueReader;


public class BuildData {

	private static CallGraph cg; 
	public static ArrayList<SootClass> applicationClasses;
	public static ArrayList<SootClass> librairyClasses;
	private static ArrayList<SootMethod> methodM;
	
	public static List<classDescription> classesD=new ArrayList<classDescription>();
	
	protected static HashMap<SootClass,classDescription> classesDD= new HashMap<SootClass,classDescription>();
		
	public ArrayList<SootClass> getClassM ()
	
	{return applicationClasses;}
	
	
	public BuildData (CallGraph cg, ArrayList<String> classes)
	
	{
		this.cg = cg;		
		applicationClasses = new ArrayList<SootClass>();
		methodM = new ArrayList<SootMethod>();
		
		classDescription cl;
		
		for (SootClass c : Scene.v().getClasses()) {
			if (classes.contains(c.toString())){
				applicationClasses.add(c);
				//System.out.println(c);							
			}
		}
				
		QueueReader<Edge> reader = cg.listener();
	        while(reader.hasNext()) {
	            Edge e = (Edge) reader.next();
		    if(!(e.toString().contains("forName(")  || e.toString().contains("newInstance()"))) {
	           	SootMethod m1 = e.src();
	            SootMethod m2 = e.tgt();
	        	if (!methodM.contains(m1) && applicationClasses.contains(m1.getDeclaringClass()))
	        		methodM.add(m1);
	            	if (!methodM.contains(m2) && applicationClasses.contains(m2.getDeclaringClass()))
	            		methodM.add(m2);
		    }
		    else
		    	cg.removeEdge(e);
			
	        }
	       
	        int i=0;
	        List<String> output = new ArrayList<String>();
	        
	       	Iterator it=applicationClasses.iterator();
			while (it.hasNext()){
				SootClass c=(SootClass)it.next();
				cl=new classDescription(c);
				classesD.add(cl);
				classesDD.put(c, cl);
				System.out.println("------- done");
			}		
	}
	
	public List<String> buildPredicates(){
		List<String> output = new ArrayList<String>();
		int i=0;
		
		for(classDescription cl : classesD){
			System.out.println(i+" "+cl.name);
			
			
			output.add("StartClass "+i);
			
			String s="Class("+cl.name+",";
			if(cl.classe.isInterface())
				s+="Y,";
			else
				s+="N,";
			if(cl.classe.isAbstract())
					s+="Y,";
				else
					s+="N,";
			
			if(cl.classe.isPrivate())
				s+="Private";
			else if(cl.classe.isPublic())
				s+="Public";
			else if(cl.classe.isProtected())
				s+="Protected";
			else s+="NA";
			s+=");";			
			output.add(s);
			
					
			s = new String();
			Iterator iter=cl.fields.iterator();
			while(iter.hasNext()){
				SootField f=(SootField)iter.next();
				s="Attribute("+f.getName()+","+f.getType()+",";
				if(f.isPrivate())
					s+="Private,";
				else
					if(f.isPublic())
						s+="Public,";
					else
						if(f.isProtected())
							s+="Protected,";
						else
							s+="Public,"; // par défaut un attribut est public
				
				if(f.isStatic())
					s+="Y,";
				else 
					s+="N,";
				
				if(f.isFinal())
					s+="Y";
				else 
					s+="N";
				
				s+=");";
				
				output.add(s);
			}
			
			s = new String();
			iter=cl.methods.iterator();
			while(iter.hasNext()){
				SootMethod m = (SootMethod)iter.next();
				s="Method("+m.getName()+",";
				
				s+=m.getReturnType()+",";
				
				if(m.isPublic())
					s+="Public,";
				else
					if(m.isPrivate())
						s+="Private,";
					else
						if(m.isProtected())
							s+="Protected,";
						else
							s+="Public,";
				
				if(m.isStatic())
					s+="Y,";
				else
					s+="N,";
				
				if(m.isAbstract())
					s+="Y";
				else
					s+="N";
				
				s+=");";
				
				output.add(s);
			}
			
			s = new String();
			iter=cl.methods.iterator();
			while(iter.hasNext()){
				SootMethod m = (SootMethod)iter.next();
				for(Object p : m.getParameterTypes()){
					s = new String();
					s = "Parameter("+m.getName()+","+p.toString()+");";
						output.add(s);
				}
			}
			
			
			s = new String();
			iter=cl.superClasses.iterator();
			while(iter.hasNext()){
				SootClass c = (SootClass)iter.next();
				s="SuperClass("+c.getName()+");";
				output.add(s);
			}
			
			s = new String();
			iter=cl.subClasses.iterator();
			while(iter.hasNext()){
				SootClass c = (SootClass)iter.next();
				s="SubClass("+c.getName()+");";
				output.add(s);
			}
			
			s = new String();
			iter=cl.methods.iterator();
			while(iter.hasNext()){
				SootMethod m=(SootMethod)iter.next();
				Iterator it = callOut(m).iterator();
				while(it.hasNext()){
					SootMethod calledMethod = (SootMethod)it.next();
					s="CallOut("+m.getDeclaringClass().getName()+","+m.getName()+" -->  "+calledMethod.getName()+","+calledMethod.getDeclaringClass()+");";
					output.add(s);
				}
			}
			
			//internal call between methods of the same class
			s = new String();
			iter=cl.methods.iterator();
			while(iter.hasNext()){
				SootMethod m=(SootMethod)iter.next();
				Iterator targets = new Targets(cg.edgesOutOf(m));
				while (targets.hasNext()) {		
					SootMethod tgt = (SootMethod)targets.next();
					if(m.getDeclaringClass().equals(tgt.getDeclaringClass())){
						s="InternalCall("+m.getDeclaringClass().getName()+","+m.getName()+" -->  "+tgt.getName()+","+tgt.getDeclaringClass()+");";
						output.add(s);
					}
				}	
			}
			
			/*
			System.out.println("");
			System.out.print("Associations: ");
			iter=cl.Assoc.iterator();
			while(iter.hasNext()){
				System.out.print(((SootClass)iter.next()).getName()+" ");
			}
			System.out.println("");
			
			
			
			System.out.print("Attribute RW: ");
			iter=cl.attributRW.iterator();
			while(iter.hasNext()){
				System.out.print(((SootClass)iter.next()).getName()+" ");
			}
			System.out.println("");
			
			
			
			System.out.print("callIn: ");
			set=cl.callIn.keySet();
			iter=set.iterator();
			System.out.println(cl.callIn.size());
			while(iter.hasNext()){
				SootMethod m=(SootMethod)iter.next();
				System.out.println("\t  "+m+" --------  "+cl.callIn.get(m));
			}
			System.out.println("");
			*/
			
			
			output.add("EndClass "+i);
			i++;
		}
			
		return output;
	}
	
	 public static List<SootClass> superClasses(SootClass classe) {
		 List<SootClass> sc = new ArrayList<SootClass>();
         if(!classe.isInterface()){
             for (SootClass  c: Scene.v().getActiveHierarchy().getSuperclassesOf(classe))
                 if(applicationClasses.contains(c))
                     sc.add(c);}
         return sc;
     }
	
	public static List<SootClass> subClasses(SootClass classe) {
		List<SootClass> sc = new ArrayList<SootClass>();
		if(!classe.isInterface())
			for (SootClass  c: Scene.v().getActiveHierarchy().getSubclassesOf(classe)) 
				if(applicationClasses.contains(c))
					sc.add(c);
		return sc;
	}
	
	public static List<SootMethod> callOut(SootMethod method) {
		List<SootMethod> couple = new ArrayList<SootMethod>();

		Iterator targets = new Targets(cg.edgesOutOf(method));
		while (targets.hasNext()) {		
		//	Edge e = (Edge)targets.next();
		//	SootMethod tgtMethod = e.getTgt().method();
			SootMethod tgt = (SootMethod)targets.next();
			if (methodM.contains(tgt) && !tgt.getDeclaringClass().equals(method.getDeclaringClass()) && !couple.contains(tgt))
				couple.add(tgt);
		}
		return couple;
	}
	// ensemble des methodes qui appel method
	public  static List<SootMethod> callIn(SootMethod method) {
		List<SootMethod> couple = new ArrayList<SootMethod>();

		Iterator targets = cg.edgesInto(method);
		while (targets.hasNext()) {		
			Edge e = (Edge)targets.next();
			SootMethod tgt = e.getSrc().method();
			//SootMethod tgt = (SootMethod)targets.next();
			if (methodM.contains(tgt) && !tgt.getDeclaringClass().equals(method.getDeclaringClass()))
			couple.add(tgt);
		}
		return couple;
	}
	
	public static HashMap<SootMethod, List<SootMethod>> callOut(classDescription cl){
		
		HashMap<SootMethod, List<SootMethod>> r=new HashMap<SootMethod, List<SootMethod>>();
		for(SootMethod m : cl.methods){
			if(!callOut(m).isEmpty())
			r.put(m, callOut(m));
		}
		return r;	
	}
	
	public static HashMap<SootMethod, List<SootMethod>> callIn(classDescription cl){
		
		HashMap<SootMethod, List<SootMethod>> r=new HashMap<SootMethod, List<SootMethod>>();
		for(SootMethod m : cl.methods){
			if(!callIn(m).isEmpty())
			r.put(m, callIn(m));
		}
		return r;
	}
	
	public static  boolean call(SootMethod source, SootMethod target) 
	{
		Iterator targets = new Targets(cg.edgesOutOf(source));
		while (targets.hasNext()) {		
			SootMethod tgt = (SootMethod)targets.next();
			if(target.equals(tgt) && target.getDeclaringClass().equals(source.getDeclaringClass()))
				return true;
		}
		return false;	
	}
	
	public static List<SootClass> getAssoc(classDescription classe)
	
	{
		List<SootClass> list = new ArrayList<SootClass>();
		for (SootField field : classe.fields) {
			try {
			SootClass tmp = Scene.v().getSootClass(field.getType().toString().split(" ")[0]);
			if(applicationClasses.contains(tmp))
				list.add(tmp);
			}
			catch(Exception o) { }
			
		} 
		return list;		
	}
	
	public static List<SootClass> getAggreg(SootClass classe){
		
		List<SootClass> list = new ArrayList<SootClass>();
		for (SootMethod meth : methodM) {
			if(meth.toString().contains("<init>"))
				//System.out.println(meth);
				for (Object tmp : meth.getParameterTypes()) {
					Type type = (Type)tmp;
					//System.out.println("  type: "+type);
					try {
						SootClass tmp2 = Scene.v().getSootClass(type.toString().split(" ")[0]);
						if(classe.toString().compareTo(tmp2.toString()) == 0){
							//System.out.println("ajout de "+meth.getDeclaringClass());
							list.add(meth.getDeclaringClass());
						}
						}
						catch(Exception o) { }
				}
		}
		return list;
	}
	
	
	public  int nbinvoke(SootClass source, SootClass target) 
	{
		
		int nb = 0;
		for (SootMethod method : source.getMethods()) {
			Iterator targets = new Targets(cg.edgesOutOf(method));
			while (targets.hasNext()) {		
				SootMethod tgt = (SootMethod)targets.next();
				if(target.getMethods().contains(tgt))
					nb++;
			}
	}	
		return nb;	
	}
	
	
	public static List<SootMethod> cohesion(classDescription source){
		
		List<SootMethod> res=new ArrayList<SootMethod>(); 
		for (SootMethod method : source.methods) {
		
			Iterator targets = new Targets(cg.edgesOutOf(method));
			while (targets.hasNext()) {		
				SootMethod tgt = (SootMethod)targets.next();
				if(method.getDeclaringClass().equals(tgt.getDeclaringClass())){
					//res.add(tgt.getDeclaringClass());
					res.add(tgt);
				}
			}
		}	
		return res;	
	}
	
	
	protected static List<SootClass> attributRW(SootMethod method){
		List<SootClass> att = new ArrayList<SootClass>();
		if (method.hasActiveBody()) {
			for (Unit ut : method.getActiveBody().getUnits())
				for (ValueBox vb : ut.getUseBoxes())
					if (vb.getValue() instanceof soot.jimple.FieldRef){
						SootClass cc = ((soot.jimple.FieldRef)vb.getValue()).getField().getDeclaringClass();
						if(applicationClasses.contains(cc))
							att.add(cc);
					}
		}
		return att;
	}
	
	public static List<SootClass> _attributRW(classDescription classe){
		List<SootClass> att = new ArrayList<SootClass>();
		for(SootMethod method : classe.methods){
			if (method.hasActiveBody()) {
				for (Unit ut : method.getActiveBody().getUnits())
					for (ValueBox vb : ut.getUseBoxes())
						if (vb.getValue() instanceof soot.jimple.FieldRef){
							SootClass cc = ((soot.jimple.FieldRef)vb.getValue()).getField().getDeclaringClass();
							//System.out.println(" attributRW "+cc+" --------- method--------  "+method.getName()+"  ---  "+(((soot.jimple.FieldRef)vb.getValue())));
							if(applicationClasses.contains(cc)){
								att.add(cc);
							}
								
						}
			}
		}
		return att;
	}
	
	protected static boolean attributRW(SootClass classe, SootMethod method, SootField field){
		boolean output = false;
		if (method.hasActiveBody()) {
			for (Unit ut : method.getActiveBody().getUnits())
				for (ValueBox vb : ut.getUseBoxes())
					if (vb.getValue() instanceof soot.jimple.FieldRef){
						SootClass cc = ((soot.jimple.FieldRef)vb.getValue()).getField().getDeclaringClass();
						SootField ff = ((soot.jimple.FieldRef)vb.getValue()).getField();
						if(ff.equals(field))
							return true;
					}
		}
		return output;
	}
	
	private static List<SootClass> getApplicationClasses(){
		List<SootClass> output = new ArrayList<SootClass>();
		output.addAll(Scene.v().getApplicationClasses());
		return output;
		
	}
	
	private static List<SootClass> getLibrairyClasses(){
		List<SootClass> output = new ArrayList<SootClass>();
		output.addAll(Scene.v().getLibraryClasses());
		return output;
	}

}
