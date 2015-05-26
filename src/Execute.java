
import soot.jimple.toolkits.callgraph.CallGraph;
import java.util.List;
import java.util.ArrayList;
import java.io.*;


public class Execute {
	
	public static CallGraph cg;
	static BufferedWriter writer;
	
	
	public static void main(String[] args) {
		
		cg=CGBuilder.BuildCallGraph();
	
		System.out.println("on a un call graph cg");
		System.out.println(CGBuilder.SourcePath);
		
				
		BuildData BD = new BuildData(cg,CGBuilder.getClass(CGBuilder.SourcePath));
		
		System.out.println("Call Graph Builder");
	
		System.out.println("maxSize: "+BuildData.classesD.size());
		
	
		
		List<String> data = BD.buildPredicates();
		
		for(String predicates : data){
			System.out.println(predicates);
		}
				 
		writeFile(data, "C:\\Users\\Ali\\Desktop\\tests\\predicates stem.analyis.automaticexperiment.txt");
		
	}
	
	public static void writeFile(List<String> predicates, String file){
		try
		{
			writer=new BufferedWriter(new FileWriter(file));
			
			for(String s : predicates){
				writer.write(s);
				writer.newLine();
			}
			
			writer.close();
		 }
		 catch(FileNotFoundException e){
		 	System.out.println("FileNotFoundException: "+e.toString());
		 }
		 catch(IOException e){
		 	System.out.println("IOException: "+e.toString());
		 }
	}
}

