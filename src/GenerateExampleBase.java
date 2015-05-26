import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


public class GenerateExampleBase {
	
	
	public static void start(List<classDescription> classes, String fileName) throws IOException {
		
		FileWriter file = new FileWriter(fileName);
		Iterator it=classes.iterator();
		String s;
		while (it.hasNext()){
			classDescription c=(classDescription)it.next();
			s=c.name+" "+c.nbFields+" "+c.nbMethods+" "+c.nbSubClasses+" "+c.nbsuperClasses+" "+c.callIn.size()+" "+c.callOut.size()+" "+c.cohesion.size()+" "+c.nbAssoc;
			file.write(s+"\n");
		}
		file.close();
		////////////////////
	}
	
	public static void parseCode(HashMap<SootClass,classDescription> classes, String fileName) throws IOException{
		
		FileWriter file = new FileWriter(fileName);
		Iterator it=classes.keySet().iterator();
		String s;
		int i=0;
		while (it.hasNext()){
			classDescription cl=classes.get((SootClass)it.next());
			file.write("________________________________________________________________________________________________\n");
			//s=c.name+" "+c.nbFields+" "+c.nbMethods+" "+c.nbSubClasses+" "+c.nbsuperClasses+" "+c.fanOut.size()+" "+c.fanIn.size()+" "+c.cohesion.size()+" "+c.nbAssoc;
			
			s=new String();
			s=i+"  "+cl.name;
			s+="nbMethods: "+cl.nbMethods+" nbFields: "+cl.nbFields+"\n";
							
			s+="Fields: ";
			Iterator iter=cl.fields.iterator();
			while(iter.hasNext()){
				s+=(((SootField)iter.next()).getName()+" ");
			}
			s+="\n";
			
			
			s+="methods: ";
			iter=cl.methods.iterator();
			while(iter.hasNext()){
				s+=((SootMethod)iter.next()).getName()+" ";
			}
			
			
			s+="\n";
			s+="Vocabulary: ";
			iter=cl.vocabulary.iterator();
			while(iter.hasNext()){
				s+=((String)iter.next())+" ";
			}
			
			s+="\n";							
			s+="subClasses: ";
			iter=cl.subClasses.iterator();
			while(iter.hasNext()){
				s+=((SootClass)iter.next()).getName()+" ";
			}
			
			
			s+="\n";
			s+="superClasses: ";
			iter=cl.superClasses.iterator();
			while(iter.hasNext()){
				s+=((SootClass)iter.next()).getName()+" ";
			}
			
			
			s+="\n";
			s+="Associations: ";
			iter=cl.Assoc.iterator();
			while(iter.hasNext()){
				s+=((SootClass)iter.next()).getName()+" ";
			}
			s+="\n";
			
			
			s+=("cohesion: ");
			iter=cl.cohesion.iterator();
			while(iter.hasNext()){
				s+=((SootMethod)iter.next()).getName()+" ";
			}
			s+="\n";
			
			s+=("Attribute RW: ");
			iter=cl.attributRW.iterator();
			while(iter.hasNext()){
				s+=(((SootClass)iter.next()).getName()+" ");
			}
			s+="\n";
			
			s+=("callOut: ");
			Set set=cl.callOut.keySet();
			iter=set.iterator();
			s+=(cl.callOut.size());
			while(iter.hasNext()){
				SootMethod m=(SootMethod)iter.next();
				s+=("\t  "+m+" --------  "+cl.callOut.get(m));
			}
			s+="\n";
			
			s+=("callIn: ");
			set=cl.callIn.keySet();
			iter=set.iterator();
			s+=(cl.callIn.size());
			while(iter.hasNext()){
				SootMethod m=(SootMethod)iter.next();
				s+=("\t  "+m+" --------  "+cl.callIn.get(m));
			}
			s+="\n";
			
			
			file.write(s+"\n");
						
		}
		file.close();
		System.out.println("ok ");
	}
	
	
}
