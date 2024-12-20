package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;

public class MJCodeGenTest {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(MJParserTest.class);
		
		Reader br = null;
		try {
			File sourceCode = new File("test/testovi_avgust/test303.mj");
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  //pocetak parsiranja
	        
	        Program prog = (Program)(s.value); 
	        Tabb.init();
	        
			// ispis sintaksnog stabla
			// log.info(prog.toString(""));
	        
	        // ispis potencijalne greske u parseru
	        System.out.println("===============================================================");
	        if (p.errorDetected) System.out.println("Parser found error!");
	        
			System.out.println("===============================================================");

			// ispis prepoznatih programskih konstrukcija
			SemanticAnalyzer v = new SemanticAnalyzer();
			prog.traverseBottomUp(v); 
	      
			// log.info(" Print count calls = " + v.printCallCount);
			NewVisitor nv = new NewVisitor();
			Tabb.dump(nv);
			
			System.out.println("===============================================================");
			
			if (!p.errorDetected && !v.errorDetected) {
				File objFile = new File("test/program.obj");
				if (objFile.exists()) objFile.delete();
				
				CodeGenerator codeGenerator = new CodeGenerator();
				codeGenerator.globData = v.nVars + CodeGenerator.numTemp;
				prog.traverseBottomUp(codeGenerator);
				
				Code.dataSize = codeGenerator.globData;
				Code.mainPc = codeGenerator.mainPc;
				
				Code.write(new FileOutputStream(objFile));
				log.info("Prevodjenje uspesno zavrseno!");
			}
			else {
				log.info("Prevodjenje nije uspesno zavrseno!");
			}
			System.out.println("===============================================================");
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}
	
	
}