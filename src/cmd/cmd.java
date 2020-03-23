package cmd;


import java.io.File;
import java.util.Random;
import main.Arquivo;

public class cmd {
    public static final String CMD_PATH_TEMPDIR = System.getProperty("java.io.tmpdir");
    public static final String CMD_PATH_C = "C:\\";
    public static final String CMD_PATH_USER = System.getProperty("user.home");
    
    /**
     * Cria um arquivo .bat na pasta temp, executa o script que se auto destroi depois.\n
     * Se responsabiliza apenas por executar o script e não por receber a resposta do mesmo.
     * @param script Codigo para o arquivo.bat
     * @return Verdadeiro se conseguir criar o arquivo e executar, falso se não conseguir criar ou executar o arquivo.
     */
    public static boolean execute_script(String script){
        return execute_script(script, CMD_PATH_TEMPDIR);
    }
    
    
    /**
     * Cria um arquivo .bat no local indicado, executa o script que se auto destroi depois.\n
     * Se responsabiliza apenas por executar o script e não por receber a resposta do mesmo.
     * @param script Codigo para o arquivo.bat
     * @param parentPath Pasta onde o script será executado
     * @return Verdadeiro se conseguir criar o arquivo e executar, falso se não conseguir criar ou executar o arquivo.
     */
    public static boolean execute_script(String script, String parentPath){
        boolean b = true;
        
        try{
            Arquivo a = new Arquivo();
            Random r = new Random();

            String nome_bat = "";
            for (int i = 0; i < 10; i++) {
                nome_bat += "" + r.nextInt(9);
            }
            String caminho_arquivo = parentPath + nome_bat + ".bat";

            //INSERE AUTO DESTRUIÇÃO
            script += "\ndel \"%~f0\""
                    + "\nEXIT"
                    + "\ndel \"%~f0\"";

            if(Arquivo.salvar(caminho_arquivo, script)){
                try { 
                    java.awt.Desktop.getDesktop().open(new File(caminho_arquivo));
                    
                } catch (Exception e) {
                    System.out.println("Problema ao abrir arquivo!");
                    e.printStackTrace();
                    b = false;
                }
            }else{
                System.out.println("Não salvou o arquivo (" + caminho_arquivo + ")");
            }            
        }catch(Exception e){
            System.out.println("Ocorreu algum erro ao tentar criar e executar o CMD:" + e);
            e.printStackTrace();
            b = false;
        }
        return b;
    }
    
    /**
     * Executa um código cmd diretamente pelo Runtime Exec do java
     * @param cmd Codigo cmd a ser executado
     * @return Os retornos dos códigos executados.
     * @throws java.io.IOException
     */
    public static String execCmd(String cmd) throws java.io.IOException {
        java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
