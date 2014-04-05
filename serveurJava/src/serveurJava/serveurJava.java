package serveurJava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class serveurJava {
	static final int port = 8080;

	public static void main(String[] args) throws Exception {
		System.out.println("LANCEMENT DU SERVEUR");
		ServerSocket s = new ServerSocket(port);
		Socket soc = s.accept();
		System.out.println("j'ai passé le acceptttttt");
		// Un BufferedReader permet de lire par ligne.
		BufferedReader plec = new BufferedReader(new InputStreamReader(soc.getInputStream()));

		PrintWriter pred = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())),true);

		BufferedReader lecteurAvecBuffer = null;
		String ligne;

		try
		{
			lecteurAvecBuffer = new BufferedReader(new FileReader("test.txt"));
		}
		catch(FileNotFoundException exc)
		{
			System.out.println("Erreur d'ouverture");
		}
		while ((ligne = lecteurAvecBuffer.readLine()) != null)
		{
			System.out.println(ligne);
			pred.println(ligne);
		}
		lecteurAvecBuffer.close();

		//String str = "bonjour";
		/*for (int i = 0; i < 10; i++) {
			pred.println(str);          // envoi d'un message
		}*/
		plec.close();
		pred.close();
		soc.close();
	}
}
