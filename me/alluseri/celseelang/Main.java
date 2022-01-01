package me.alluseri.celseelang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static String fallback(Scanner s) {
		System.out.println("Please enter the code you want to execute.");
		return s.nextLine();
	}
	public static void main(String args[]) throws FileNotFoundException {
		Scanner s = new Scanner(System.in);
		String code = "";
		if (args.length >= 2)
			switch (args[0]) { // lmao ik this is awful
			case "--code":
				code = args[1];
				break;
			case "--file":
				StringBuilder sb = new StringBuilder();
				for (int i = 1;i < args.length;i++) {
					sb.append(args[i]);
				}
				File f = new File(sb.toString());
				if (!f.exists() || f.isDirectory() || !f.canRead()) {
					code = fallback(s);
					break;
				}
				try (BufferedReader fr = new BufferedReader(new FileReader(f))) { // Shouldn't throw FNFE cuz f.exists() check
					code = fr.readLine();
				} catch (IOException e1) {
					System.out.println("An error has occured while reading your input file:");
					e1.printStackTrace();
					s.close();
					return;
				}
				break;
			default:
				code = fallback(s);
				break;
			}
		else code = fallback(s);
		code = code.trim().replaceAll("\\s", "");
		System.out.println("Executing "+code+"\n");

		int ENV = 0;
		String[] spl = code.split("");
		int sub = -1;
		for (int e = 0; e < code.length();) {
			char c = code.toCharArray()[e];
			if (sub > -1) {
				c = (char)sub;
			}
			sub = -1;
			switch (c) {
			case '0': // NOP
				break;
			case '1': // Jump
				e = spl[++e] == "$" ? ENV : Integer.parseInt(spl[e]);
				continue;
			case '2': {
				int A = spl[++e] == "$" ? ENV : Integer.parseInt(spl[e]);
				int B = spl[++e] == "$" ? ENV : Integer.parseInt(spl[e]);
				int C = spl[++e] == "$" ? ENV : Integer.parseInt(spl[e]);
				e = ENV == A ? B : C;
				} continue;
			case '3': {
				int A = spl[++e] == "$" ? ENV : Integer.parseInt(spl[e]);
				int B = spl[++e] == "$" ? ENV : Integer.parseInt(spl[e]);
				int C = spl[++e] == "$" ? ENV : Integer.parseInt(spl[e]);
				e = ENV != A ? B : C;
				} continue;
			case '4':
				ENV++;
				break;
			case '5':
				ENV--;
				break;
			case '6':
				ENV = spl[++e] == "$" ? ENV : Integer.parseInt(spl[e]);
				break;
			case '7':
				System.out.print(ENV);
				break;
			case '8':
				System.out.println(ENV);
				break;
			case '9':
				System.out.print((char)ENV);
				break;
			case 'A': case 'a':
				System.out.println((char)ENV);
				break;
			case 'B': case 'b':
				ENV += spl[++e] == "$" ? ENV : Integer.parseInt(spl[e]);
				break;
			case 'C': case 'c':
				ENV -= spl[++e] == "$" ? ENV : Integer.parseInt(spl[e]);
				break;
			case '$':
				c = String.format("%X",ENV).charAt(0);
				sub = c;
				continue;
			default:
				break;
			}
			e++;
		}
		System.out.println("\n\nFinished execution!");
		s.close();
	}
}








































