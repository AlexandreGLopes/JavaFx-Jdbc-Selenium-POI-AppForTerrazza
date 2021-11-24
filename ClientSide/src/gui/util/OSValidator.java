package gui.util;

public class OSValidator {
	
	//Pegano o nome do sistema operacional e colocando em lowerCase
	private static String os = System.getProperty("os.name").toLowerCase();
	
	//Método secundário, que vai retornar o nome do sistema operacional
		public static String checkOSName() {
			if (isWindows()) {
	            return "windows";
	        } else if (isMac()) {
	        	return "Seu sistema operacional não é suportado por esta aplicação";
	        } else if (isUnix()) {
	        	return "linux";
	        } else if (isSolaris()) {
	        	return "Seu sistema operacional não é suportado por esta aplicação";
	        } else {
	        	return "Your OS is not supported by Java!!";
	        }
		}
	
	//Métodos que leem a string e retornam os booleanos para testarmos o sistema operacional 
	public static boolean isWindows() {
        return (os.indexOf("win") >= 0);
    }
	
    public static boolean isMac() {
        return (os.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (os.indexOf("nix") >= 0
                || os.indexOf("nux") >= 0
                || os.indexOf("aix") > 0);
    }

    public static boolean isSolaris() {
        return (os.indexOf("sunos") >= 0);
    }

}
