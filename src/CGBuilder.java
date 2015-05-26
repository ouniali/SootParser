import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;

import iceberg.util.Strings;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;


public class CGBuilder {
	/*
	public static String compiledcodeFolder="bin";
	public static String SourcePath ="C:\\Users\\ouniali\\Desktop\\Tests\\Xerces\\guehene-dpl-xerces v.1.4.2\\bin";
	public static String jarLibFolder="C:\\Users\\ouniali\\Desktop\\Tests\\Xerces\\guehene-dpl-xerces v.1.4.2\\tools";
	public static String mainClass="org.apache.xerces.domx.XGrammarWriter";
	public static String JDK_HOME="C:\\Program Files\\Java\\jdk1.7.0_03\\";
	*/
	/*
	public static String compiledcodeFolder="bin";
	public static String SourcePath ="D:\\Dropbox\\Workspace\\Data\\guehene-dpl-xerces v.1.4.2\\bin";
	public static String jarLibFolder="D:\\Dropbox\\Workspace\\Data\\guehene-dpl-xerces v.1.4.2\\tools";
	public static String mainClass="org.apache.xerces.domx.XGrammarWriter";
	public static String JDK_HOME="C:\\Program Files\\Java\\jdk1.7.0_03\\";
	*/
	/*
	public static String compiledcodeFolder="bin";
	public static String SourcePath ="E:\\Dropbox\\Workspace\\Components\\Test\\org.eclipse.stem.core\\bin";
	public static String jarLibFolder="E:\\Dropbox\\Workspace\\Components\\stem_2-0-0\\org.eclipse.stem.core\\tools";
	public static String mainClass="org.apache.xml.resolver.apps.xparse";//"org.apache.xerces.impl.Version";
	public static String JDK_HOME="C:\\Program Files\\Java\\jdk1.7.0_71\\";
	*/
	
	public static String compiledcodeFolder="bin";
	public static String SourcePath ="E:\\Dropbox\\Workspace\\Data\\xerces-2_7_0\\bin";
	public static String jarLibFolder="E:\\Dropbox\\Workspace\\Data\\xerces-2_7_0\\tools";
	public static String mainClass="org.apache.xml.resolver.apps.xparse";//"org.apache.xerces.impl.Version";
	public static String JDK_HOME="C:\\Program Files\\Java\\jdk1.7.0_71\\";
	
	
	/*
	public static String compiledcodeFolder="bin";
	public static String SourcePath ="D:\\Dropbox\\Workspace\\Data\\apache-xerces2-j-cdbc0f1\\bin";
	public static String jarLibFolder="D:\\Dropbox\\Workspace\\Data\\apache-xerces2-j-cdbc0f1\\tools";
	public static String mainClass="org.apache.xmlcommons.Version";//"org.apache.xml.resolver.apps.xparse";
	public static String JDK_HOME="C:\\Program Files\\Java\\jdk1.7.0_03\\";
	*/
/*
	public static String compiledcodeFolder="org";
	public static String SourcePath ="D:\\Dropbox\\Workspace\\Data\\jhotdraw6\\org";
	public static String jarLibFolder="D:\\Dropbox\\Workspace\\Data\\jhotdraw6\\lib";
	public static String mainClass="org.jhotdraw.simples.javadraw.JavaDrawApp";
	public static String JDK_HOME="C:\\Program Files\\Java\\jdk1.7.0_03\\";

*/	
	/*
	public static String compiledcodeFolder="bin";
	public static String SourcePath ="D:\\Dropbox\\Workspace\\jfreechart-1.0.9\\bin";
	public static String jarLibFolder="D:\\Dropbox\\Workspace\\jfreechart-1.0.9\\lib";
	public static String mainClass="main.org.apache.tools.ant.launch.Launcher";
	public static String JDK_HOME="C:\\Program Files\\Java\\jdk1.7.0_03\\";
	*/
	/*
	public static String compiledcodeFolder="bin";
	public static String SourcePath ="D:\\Dropbox\\Workspace\\Data\\JHotDraw6.1\\bin";
	public static String jarLibFolder="D:\\Dropbox\\Workspace\\Data\\JHotDraw6.1\\lib";
	public static String mainClass="org.jhotdraw.samples.javadraw.JavaDrawApp";
	public static String JDK_HOME="C:\\Program Files\\Java\\jdk1.7.0_03\\";
	*/
/*
	public static String compiledcodeFolder="bin";
	public static String SourcePath ="C:\\Users\\Ali\\Desktop\\ganttproject-2.0\\bin";
	public static String jarLibFolder="C:\\Users\\Ali\\Desktop\\ganttproject-2.0\\lib";
	public static String mainClass="org.ganttproject.GanttProject";//"org.apache.poi.hssf.record.RKRecord";
	public static String JDK_HOME="C:\\Program Files\\Java\\jdk1.7.0_03\\";
	*/
	
	/*
	public static String compiledcodeFolder="output";
	public static String SourcePath ="C:\\Users\\ouniali\\Desktop\\Tests\\paros-3.2.3-src\\paros\\output";
	public static String jarLibFolder="C:\\Users\\ouniali\\Desktop\\Tests\\paros-3.2.3-src\\paros\\lib";
	public static String mainClass="org.parosproxy.paros.Paros";
	*/
	public ArrayList<SootClass> systemClasses=new ArrayList<SootClass>(); // sans ajouter les librairies
	
	
	public static void init() {
//		soot.options.Options.v().set_keep_line_number(true);
//		soot.options.Options.v().allow_phantom_refs();
		soot.options.Options.v().set_whole_program(true);
		
		soot.options.Options.v().setPhaseOption("cg","verbose:true");
	}

	
	public static CallGraph BuildCallGraph()  {
		
		CallGraph cg;
		init();
		System.out.println("Setting classpath");
		addJarFrom(jarLibFolder);
		//Tools.addJarFrom("D:\\Dropbox\\Workspace\\Data\\apache-xerces2-j-cdbc0f1\\tools\\bin");
		//Tools.addJarFrom("D:\\Dropbox\\Workspace\\Data\\ganttproject-2.0.9-src\\ganttproject\\lib\\development");
		
		
	    System.out.println("Setting main class");
		
		//setMainClass(mainClass);
		loadMain();
				
		System.out.println("Building CG");
		cg=buildCG();
		System.out.println("Done");
		//System.out.println(Scene.v().getMainMethod());
	    //System.out.println(Scene.v().getCallGraph());
			   
	    return cg;
	}
	
	public static void setMainClass(String className) {
		SootClass mainClass = Scene.v().loadClassAndSupport(className);
		mainClass.setApplicationClass();
		Scene.v().setMainClass(mainClass);
		List<SootMethod> ep = Scene.v().getEntryPoints();
		ep.add(Scene.v().getMainMethod());
		System.out.println("Execute Class: "+Scene.v().getMainMethod());
		Scene.v().setEntryPoints(ep);
	}
	
	
	public static CallGraph buildCG() {
		
		Scene.v().loadNecessaryClasses();
		//Scene.v().loadBasicClasses();
		CHATransformer.v().transform();
		System.out.println("public static CallGraph buildCG(): Type CHA");
		
		return Scene.v().getCallGraph();
	}
	
	
	private static void loadMainClass(String classe){
		SootClass mainClass = Scene.v().loadClassAndSupport(classe);
		mainClass.setApplicationClass();
		Scene.v().setMainClass(mainClass);
		System.out.println(classe);
		List<SootMethod> ep = Scene.v().getEntryPoints();
		ep.add(Scene.v().getMainMethod());
		Scene.v().setEntryPoints(ep);
	}
	
	public static void  loadMain(){
		for (String cl : getClass(SourcePath)) {
			// la j'essai de boucler sur l'ensemble des classes et je suppose que c un point dentrée, si c bon c bon g un point dentrée sinon lexception sera catché
	try {
			System.out.println("pt :"+cl);
			loadMainClass(cl);
	    } catch (Exception e) {}	
       
	    System.out.println("Lodgae des class main");
		}
	}
	
	public static void addJarFrom(String rep){
		String JDK_HOME = CGBuilder.JDK_HOME;//"C:\\Program Files\\Java\\jdk1.7.0\\";
		
		List<String> lst=new ArrayList<String>();
		lst.add(CGBuilder.compiledcodeFolder);
		lst.add(JDK_HOME + "jre\\lib\\rt.jar");
		lst.add(JDK_HOME + "jre\\lib\\jce.jar");
		lst.add(JDK_HOME + "jre\\lib\\javaws.jar");
		lst.add(JDK_HOME + "jre\\lib\\jsse.jar");
				
		File reper = new File(rep);
		String [] listefichiers;
		
		String jarFolder=rep.substring(rep.lastIndexOf("\\")+1);
		System.out.println(jarFolder);
		
		listefichiers=reper.list();
		for(int i=0;i<listefichiers.length;i++){
			if(listefichiers[i].endsWith(".jar")==true){ 
				lst.add(jarFolder+"\\"+listefichiers[i]);	
				System.out.println(jarFolder+"\\"+listefichiers[i]);
			}
		}
		
		String[] cp =new String[lst.size()];
		int i=0;
		Iterator it=lst.iterator();
		while(it.hasNext()){
			cp[i]=(String)it.next();
			i++;
		}
				
		Scene.v().setSootClassPath(Strings.join(cp, ";"));
		System.out.println("okkk");
	}
	
	public static ArrayList<String> getClass(String repertoire) {
		ArrayList<String> list;
		
			File rep = new File(repertoire);
			rep.getName();
			String [] listefichiers;
			list = new ArrayList<String>();
			listefichiers=rep.list();
			for(int i=0;i<listefichiers.length;i++){
				if(listefichiers[i].endsWith(".class")==true ){ 
						list.add(listefichiers[i].split(".class")[0]);
						System.out.println("addFile: ");
				}
			}
			for (File r : rep.listFiles()) {
				if(r.isDirectory()){
					list.addAll(getClass(r.getName(), r));
					System.out.println("addFile: ");
				}
					
			}
		return list;
	}
	private static ArrayList<String> getClass(String repertoire,File rep){
		
		String [] listefichiers;
		ArrayList<String> list = new ArrayList<String>();
		
		int i;
		listefichiers=rep.list();
		
		for(i=0;i<listefichiers.length;i++){
			if(listefichiers[i].endsWith(".class")==true){ 
					list.add(repertoire+"."+listefichiers[i].split(".class")[0]);
			}
		}
		for (File r : rep.listFiles()) {
			if(r.isDirectory())
				list.addAll(getClass(repertoire+"."+r.getName(), r));
		}
		return list;
	}

}
