package launcher;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import serial.Personne;

public class ObjectLoader {

	private static final Logger LOG = Logger.getLogger(ObjectLoader.class
			.getName());

	public ObjectLoader() {

	}

	public static void main() {
		InputStream in = null;
		InputStreamReader ois = null;
		BufferedReader br = null;

		try {
			in = new FileInputStream("personne.obj");
			ois = new InputStreamReader(in, "UTF-8");
			br = new BufferedReader(ois);

			StringBuilder sb = new StringBuilder();

			String line = br.readLine();

			while (br != null) {
				sb.append(line);
				line = br.readLine();

			}

			System.err.println(sb);

			String s = sb.toString();
			String[] tab = s.split(" ");

			Map<String, Integer> map = new HashMap<String, Integer>();

			for (String st : tab) {

				if (map.get(st) == null) {
					map.put(st, 1);
				} else {
					map.put(st, map.get(st) + 1);
				}

				LinkedList<Entry<String, Integer>> l = new LinkedList<Entry<String, Integer>>(
						map.entrySet());

				System.out.println();

				Collections.sort(l, new WordCompare());

				System.out.println();

				for (int i = 0; i < l.size(); i++) {
						System.out.println(l.get(i).getValue() + ", " + l.get(i).getKey());
				}

			}

		} catch (FileNotFoundException e) {
			LOG.error("err file not found" + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("err redaing object" + e);
		} finally {
			try {

				if (ois != null)
					ois.close();
				if (in != null)
					in.close();
			} catch (Exception e) {
				LOG.error("err stream closing" + e);
			}
		}

	}

}
