/**
 * Some code here is adapted from https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/.
 * This code is used to handle JSON input and output
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Main {

    private static final HashMap<String, Double> workAttributes;
    private static final HashMap<String, Double> socialAttributes;

    static {
        // initialize global variables
        // each entry into the hash tables should match the an attribute being measured in both applicants
        // and team members.
        // New attributes can be added as needed.

        // workAttributes should be attributes that have relevancy to work.
        // The values of workAttributes will be the sum of each team members' rating in that attribute.
        workAttributes = new HashMap<>();
        workAttributes.put("intelligence", 0.0);
        workAttributes.put("strength", 0.0);
        workAttributes.put("endurance", 0.0);

        // socialAttributes should be attributes that do not have clear relevancy to work.
        // The values of socialAttributes will be the average of all team members' rating in that attribute.
        socialAttributes = new HashMap<>();
        socialAttributes.put("spicyFoodTolerance", 0.0);
    }

    public static void main(String[] args) {
        // Cover if improper input is received
        if (args.length != 1) {
            System.out.println("Please include exactly one command-line argument, which is the file path for your JSON file.");
            return;
        }
        // read in JSON file
        JSONObject teamAndApplicantList = readJsonFile(args[0]);
        assert teamAndApplicantList != null;
        int teamSize = parseTeam(teamAndApplicantList);
        JSONArray applicantsList = (JSONArray) teamAndApplicantList.get("applicants");

        // create instance of compatibility checker
        CompatibilityPredictor predictor = new CompatibilityPredictor(applicantsList, workAttributes, socialAttributes, teamSize);
        JSONArray scoredApplicants = predictor.createScoredApplicants();
        System.out.println(scoredApplicants);


        // format into JSON file
        JSONObject JSONOutput = new JSONObject();
        JSONOutput.put("scoredApplicants", scoredApplicants);
        writeFile(JSONOutput);

    }


    /**
     * Using code adapted from https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/
     * read in specified JSON file.
     *
     * @param inputFile name of JSON file to be read.
     */
    private static JSONObject readJsonFile(String inputFile) {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(inputFile)) {
            //Read JSON file
            return (JSONObject) jsonParser.parse(reader);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Pulls all attribute scores from team members and adds them into the attributes hashmaps.
     */
    private static int parseTeam(JSONObject teamAndApplicantList) {
        // get the team members in a JSON Array so we can iterate through it
        JSONArray team = (JSONArray) teamAndApplicantList.get("team");
        for (Object teamMemberObject : team) {
            JSONObject teamMember = (JSONObject) teamMemberObject;
            JSONObject attributes = (JSONObject) teamMember.get("attributes");

            workAttributes.replaceAll((attribute, sum) -> workAttributes.get(attribute) + ((Long) attributes.get(attribute)).intValue());

            socialAttributes.replaceAll((attribute, sum) -> socialAttributes.get(attribute) + (Long) attributes.get(attribute));
        }
        socialAttributes.replaceAll((attribute, average) -> socialAttributes.get(attribute) / team.size());
        return team.size();
    }

    /**
     * Using code adapted from https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/
     * write JSON file.
     *
     * @param jsonOutput name of JSON file to be written in.
     */
    private static void writeFile(JSONObject jsonOutput) {
        //Write JSON file
        try (FileWriter file = new FileWriter("scoredApplicants.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(jsonOutput.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
