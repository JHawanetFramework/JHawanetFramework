package model.io;

import model.epanet.element.Gama;
import model.metaheuristic.problem.impl.PipeOptimizing;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Parse the gama file. It is only used for the {@link PipeOptimizing}
 *
 */
public class GamaParser {

	/**
	 * Read the values from a file in system
	 * @param file The file that contains the values
	 * @return A list of object with the gama values.
	 * @throws IOException If there is a exception in the io operation
	 * @throws NullPointerException if file is null
	 */
	public List<Gama> parser(@NotNull File file) throws IOException {
		Objects.requireNonNull(file);
		ArrayList<Gama> gamas = new ArrayList<>();
		double diameter;
		double cost;
		
		try (BufferedReader buffReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), StandardCharsets.ISO_8859_1))) {

			String line;
			while ((line = buffReader.readLine()) != null) {

				line = line.trim();
				if (line.length() == 0) {
					continue;
				}

				int semicolonIndex = line.indexOf(";");
				if (semicolonIndex != -1) {
					if (semicolonIndex > 0) {
						line = line.substring(0, semicolonIndex);
					} else {
						continue;
					}
				}

				String[] tokens = line.split("[ \t]+");
				if (tokens.length == 0)
					continue;
				diameter = Double.parseDouble(tokens[1]);
				cost = Double.parseDouble(tokens[3]);
				gamas.add(new Gama(diameter, cost));
			}
		} 
		return gamas;

	}
	
	public static void main(String[] args) {
		GamaParser gparser = new GamaParser();
		try {
			List<Gama> gamas = gparser.parser(new File("inp/HanoiHW.Gama"));
			
			for (Gama gama : gamas) {
				System.out.println(gama);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
