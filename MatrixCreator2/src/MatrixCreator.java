import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class MatrixCreator {
	public static void main(String[] args) throws IOException{
		String dataFile = "data.properties";
		
		final Project p = configProject(readData(dataFile));
		p.calculateProject();
		
		print(p, readOutputData(dataFile));
		
		IOProject.exportProject(p, "01 - CalculatedProject.csv");
		
		System.exit(0);
	}
	
	public static Project configProject(ProjectConfigData data){
		if(
			data.wpNames.length != data.wpPercentages.length ||
			data.pNames.length != data.pPercentages.length
		)
		{
			System.out.println("Wrong input @ config method (more/less segmentations than work packages?)");
			return null;
		}
		
		final ArrayList<Workpackage> workpackages = new ArrayList<Workpackage>();
		for(int i=0;i<data.wpNames.length;i++){
			workpackages.add(new Workpackage(data.wpNames[i], data.budget, data.wpPercentages[i], data.wpRoles[i], data.wpRoleSegmentation[i], i));
		}
		
		final ConsortialPartner consortialPartners = new ConsortialPartner();
		for(int i=0;i<data.pNames.length;i++){
			consortialPartners.addNewPartner(data.pNames[i], data.pPercentages[i], data.pPersons.get(i));
		}
		
		return new Project(data.budget, consortialPartners, workpackages);
	}

	public static void print(Project p, int[] outputData){
		
		final int titleColumnSize = outputData[0];
		final int columnSize = outputData[1];
		final int cSumSize = outputData[2];
		final int hSumSize = outputData[3];
		final int cSize = outputData[4];
		final int hSize = outputData[5];
		final int whitespaceSize = outputData[6];
		
		if(p.workpackagePercentagesSum != 100){
			System.out.println("---------------------------------------------------");
			System.out.println("WARNING! Sum of workpackage work amount is not 100%");
			System.out.println("---------------------------------------------------");
		}
		
		int i=0;
		for(double cppSum : p.consortialPartnerPercentagesSums){
			if(cppSum != 100){
				System.out.println("---------------------------------------------------");
				System.out.println("WARNING! Sum of Workpackage #"+i+" work amounts is not 100%");
				System.out.println("---------------------------------------------------");
			}
			i++;
		}
		
		if(p.budget<p.overallCosts){
			System.out.println("---------------------------------------------------");
			System.out.println("WARNING! Costs above budget");
			System.out.println("---------------------------------------------------");
		}
		
		//Budget
		System.out.println("Budget: "+round(p.budget));
		
		//Title Line
		System.out.print(formatOutput(titleColumnSize,0));
		for (Workpackage wp : p.workpackages){
			printCellCentered(wp.name+" ("+round(wp.percentage)+"%) " + assembleString(wp.roles) + " " + assemblePercentString(wp.roleSegmentation), columnSize);
		}
		System.out.println("Sum: "+round(p.workpackagePercentagesSum)+"%");
		//Header Line
		printCell(" ", titleColumnSize);
		for (@SuppressWarnings("unused") Workpackage wp : p.workpackages){
			printCell(getCellWithoutDelimiter("  Costs", cSize)+getCellWithoutDelimiter("Hours", hSize)+getCellWithoutDelimiter("Roles", whitespaceSize), columnSize);
		}
		System.out.println();
		//Partner Lines
		int j=0;  //partner counter
		for (Partner partner : p.consortialPartner.partners){
			printCellCentered("--"+partner.name+"--", titleColumnSize);
			
			int k=0;  //column (wp) counter
			for (@SuppressWarnings("unused") Workpackage wp : p.workpackages){
				final String costOutput = getCellWithoutDelimiter("--"+ round(p.consortialPartnerCosts[j][k]) + "€", cSize);
				final String hoursOutput = getCellWithoutDelimiter("" + round(partner.getTotalHoursForPackage(k)) + "h", hSize);
				final String whiteOutput = getCellWithoutDelimiter("", whitespaceSize);
				
				printCell(costOutput + hoursOutput + whiteOutput + "("+round(p.consortialPartnerPercentages[j][k])+"%)--", columnSize);
				k++;
			}
			
			//Partner Lines Sum
			printCellWithoutDelimiter("cSum:-" + round(p.consortialPartnerCostsSums[j])+"€-", cSumSize);
			printCellWithoutDelimiter("hSum:-" + round(partner.getTotalHours())+"-", hSumSize);
			System.out.print("--" + partner.name + "--");
			
			System.out.println();
			j++;
			
			//Person Lines
			for(Person person : partner.persons){
				printCell(person.name + " " + assembleString(person.roles), titleColumnSize);
				
				int l=0;
				for(double hours : person.hoursInPackages){
					if(hours != 0){
						final String costOutput = getCellWithoutDelimiter("  "+(round(hours*person.hourly_rate))+"€", cSize);
						final String hoursOutput = getCellWithoutDelimiter(""+round(hours) + "h", hSize);
						
						printCell(costOutput + hoursOutput + mergeArrays(person.roles, p.workpackages.get(l).roles), columnSize);
					}
					else{
						printCell("  -" ,columnSize);
					}
					l++;
				}
				
				//Person Lines Sum
				printCellWithoutDelimiter("cSum: " + round(person.hourly_rate*person.getHourSum())+"€", cSumSize);
				printCellWithoutDelimiter("hSum: " + round(person.getHourSum()), hSumSize);
				System.out.print(" -"+person.name+"-");
				
				System.out.println();
			}
		}
		
		//Sum Lines
		printCell("Workpackage sums: ", titleColumnSize);
		
		j=0;
		for (Workpackage wp : p.workpackages){
			final String costOutput = getCellWithoutDelimiter("--" + round(wp.costs) + "€", cSize);
			final String hoursOutput = getCellWithoutDelimiter("" + round(wp.duration) + "h", hSize);
			final String whiteOutput = getCellWithoutDelimiter("", whitespaceSize);
			
			printCell(costOutput + hoursOutput + whiteOutput + "("+round(p.consortialPartnerPercentagesSums[j])+"%)--", columnSize);
			j++;
		}
		
		//overall sum
		System.out.print("overall costs: "+round(p.overallCosts) + " overall hours: "+round(p.overallHours));
	}
	
	public static double[] parseDoubleArray(final String[] array){
		double[] dArray = new double[array.length];
		for(int i=0; i<array.length; i++){
			dArray[i] = Double.parseDouble(array[i]);
		}
		return dArray;
	}
	
	public static double[] parsePercentDoubleArray(final String[] array){
		double[] dArray = new double[array.length];
		for(int i=0; i<array.length; i++){
			dArray[i] = Double.parseDouble(array[i].split("%")[0]);
		}
		return dArray;
	}
	
	public static String formatOutput(final int space, final int used){
		   int spacesToGo = space-used;
		   
		   String output="";
		   
		   while(spacesToGo>0){
			   output = output + " ";
			   spacesToGo--;
		   }
		   
		   return output + " | ";
	}
	
	public static String formatOutputWithoutDelimiter(final int space, final int used){
		   int spacesToGo = space-used;
		   
		   String output="";
		   
		   while(spacesToGo>0){
			   output = output + " ";
			   spacesToGo--;
		   }
		   
		   return output;
	}
	
	public static void printCell(final String cellValue, final int columnSize){
		System.out.print(cellValue+formatOutput(columnSize,cellValue.length()));
	}
	
	public static void printCellCentered(final String cellValue, final int columnSize){
		int spacesToGo = columnSize - cellValue.length();
		int margin = spacesToGo/2;

		String output = "";
		
		for(int i=0; i<margin; i++){
			output = output + " ";
		}
		output = output + cellValue;
		if(margin*2 + cellValue.length() != columnSize){
			margin++;
		}
		for(int i=0; i<margin; i++){
			output = output + " ";
		}
		output = output + " | ";

		System.out.print(output);
	}
	
	public static void printCellWithoutDelimiter(final String cellValue, final int columnSize){
		System.out.print(cellValue+formatOutputWithoutDelimiter(columnSize,cellValue.length()));
	}
	
	public static String getCellWithoutDelimiter(final String cellValue, final int columnSize){
		return cellValue+formatOutputWithoutDelimiter(columnSize,cellValue.length());
	}
	
	public static double round(final double number) {
		return Math.round(number * Math.pow(10, 2)) / Math.pow(10, 2);		
	}
	
	public static double arraySum(final double[] arr){
		double sum=0;
		for(double d : arr){
			sum+=d;
		}
		return sum;
	}
	
	public static String assembleString(final String[] arr){
		String string = "(";
		for(String str : arr){
			string = string + str +", ";
		}
		return string.substring(0, string.length()-2)+")";
	}
	
	public static String assemblePercentString(final double[] arr){
		String string = "(";
		for(double str : arr){
			string = string + str +"%, ";
		}
		return string.substring(0, string.length()-2)+")";
	}
	
	public static String mergeArrays(final String[] arr1, final String[] arr2){
		String string = "(";
		for(String str1 : arr1){
			for(String str2 : arr2){
				if(str1.equals(str2)){
					string = string + str1 + ", ";
				}
			}
		}
		return string.substring(0, string.length()-2)+")";
	}
	
	public static int getPositionOfStringInArray(final String str, final String[] arr){
		int i=0;
		for (String arrStr : arr){
			if(arrStr.equals(str)){
				return i;
			}
			i++;
		}
		return -1;
	}
	
	public static ProjectConfigData readData(String file) throws IOException {
		// ///////////////////
		// now with .properties file
		// ///////////////////

		final Properties properties = new Properties();
		final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
		properties.load(stream);
		stream.close();

		final String budgetS = properties.getProperty("budget");
		final String consortialPartnerS = properties
				.getProperty("consortialPartner");
		final String workPackagesS = properties.getProperty("workPackages");
		final String workPackageSegmentationS = properties
				.getProperty("workPackageSegmentation");

		final double budget = Double.parseDouble(budgetS);
		final String[] pNames = consortialPartnerS.split("\\s*,\\s*");
		final String[] wpNames = workPackagesS.split("\\s*,\\s*");
		final double[] wpPercentages = parsePercentDoubleArray(workPackageSegmentationS
				.split("\\s*,\\s*"));

		final double[][] pPercentages = new double[pNames.length][];
		final ArrayList<ArrayList<Person>> pPersons = new ArrayList<ArrayList<Person>>();
		int i = 0;
		for (String consortialPartner : pNames) {
			pPercentages[i] = parsePercentDoubleArray(properties.getProperty(
					"consortialPartner." + consortialPartner + ".segmentation")
					.split("\\s*,\\s*"));

			final String[] segmentedPeopleProperty = properties.getProperty(
					"consortialPartner." + consortialPartner + ".people")
					.split("\\s*;\\s*"); // segmented into "(..., ...)" strings
			pPersons.add(new ArrayList<Person>());
			for (int j = 0; j < segmentedPeopleProperty.length; j++) {
				final String personData = segmentedPeopleProperty[j];

				final String personName = personData.substring(1,
						segmentedPeopleProperty[j].length() - 2).split(
						"\\s*,\\s*")[0];
				final double personRate = Double.parseDouble(personData
						.substring(1, segmentedPeopleProperty[j].length() - 1)
						.split("\\s*,\\s*")[1]);
				final String[] personRoles = properties.getProperty(
						"consortialPartner." + consortialPartner + "."
								+ personName).split("\\s*,\\s*");

				pPersons.get(i).add(
						new Person(personName, personRate, personRoles,
								consortialPartner, wpNames.length));

				// configure constraints for this person
				final String constraintsS = properties.getProperty(
						"consortialPartner." + consortialPartner + "."
								+ personName + ".constraints", null);
				if (constraintsS != null) {
					final String[] constraints = constraintsS
							.split("\\s*,\\s*");
					for (String constraint : constraints) {
						final String[] splitConstraint = constraint
								.split("\\.");
						final int wpPosition = getPositionOfStringInArray(
								splitConstraint[0], wpNames);
						// merge last 2 splits if there is a . in the number of
						// hours
						if (splitConstraint.length == 4) {
							splitConstraint[2] = splitConstraint[2] + "."
									+ splitConstraint[3];
						}
						//
						if (splitConstraint[1].equals("maxH")) {
							pPersons.get(i).get(j).maxHoursInPackages[wpPosition] = Double
									.parseDouble(splitConstraint[2]);
						}
					}
				}
			}

			i++;
		}
		// configure roles needed for workpackages
		final String[][] wpRoles = new String[wpNames.length][];
		final double[][] wpRoleSegmentation = new double[wpNames.length][];
		i = 0;
		for (String workPackage : wpNames) {
			final String[] segmentedWPSegmentationProperty = properties
					.getProperty("workPackage." + workPackage + ".segmentation")
					.split("\\s*;\\s*"); // segmented into "(..., ...)" strings
			wpRoles[i] = new String[segmentedWPSegmentationProperty.length];
			wpRoleSegmentation[i] = new double[segmentedWPSegmentationProperty.length];
			for (int j = 0; j < segmentedWPSegmentationProperty.length; j++) {
				final String[] wpRoleData = segmentedWPSegmentationProperty[j]
						.substring(1,
								segmentedWPSegmentationProperty[j].length() - 2)
						.split("\\s*,\\s*");
				wpRoles[i][j] = wpRoleData[0];
				final String percent = wpRoleData[1];
				wpRoleSegmentation[i][j] = Double.parseDouble(percent);
			}
			i++;
		}
		
		return new ProjectConfigData(budget, wpNames, wpPercentages, pNames, pPercentages, wpRoles, wpRoleSegmentation, pPersons);
	}
	
	public static int[] readOutputData(String file) throws IOException{
		final Properties properties = new Properties();
		final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
		properties.load(stream);
		stream.close();
		
		int[] outputConfig = new int[7];
		
		outputConfig[0] = Integer.parseInt(properties.getProperty("titleColumnSize"));
		outputConfig[1] = Integer.parseInt(properties.getProperty("columnSize"));
		outputConfig[2] = Integer.parseInt(properties.getProperty("cSumSize"));
		outputConfig[3] = Integer.parseInt(properties.getProperty("hSumSize"));
		outputConfig[4] = Integer.parseInt(properties.getProperty("cSize"));
		outputConfig[5] = Integer.parseInt(properties.getProperty("hSize"));
		outputConfig[6] = Integer.parseInt(properties.getProperty("whitepaceSize"));
		
		return outputConfig;
	}

}