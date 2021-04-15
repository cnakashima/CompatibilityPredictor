import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;

import static java.lang.Math.abs;

public class CompatibilityPredictor {

    private final JSONArray applicantsList;
    private final HashMap<String, Double> workAttributes;
    private final HashMap<String, Double> socialAttributes;
    private final int teamSize;


    public CompatibilityPredictor(JSONArray applicantsList, HashMap<String, Double> workAttributes, HashMap<String, Double> socialAttributes, int teamSize) {
        this.applicantsList = applicantsList;
        this.workAttributes = workAttributes;
        this.socialAttributes = socialAttributes;
        this.teamSize = teamSize;
    }

    /**
     * Creates a JSONArray of applicants used to write the JSON at the end
     *
     * @return JSON array of applicants JSONObjects, containing only namnes for now.
     */
    public JSONArray createScoredApplicants() {
        JSONArray scoredApplicantsList = new JSONArray();
        for (Object applicant : applicantsList) {
            JSONObject JSONApplicant = (JSONObject) applicant;
            JSONObject applicantName = new JSONObject();
            applicantName.put("name", JSONApplicant.get("name"));
            applicantName.put("score", score(JSONApplicant));
            scoredApplicantsList.add(applicantName);
        }
        return scoredApplicantsList;
    }

    /**
     * calculates a score for an applicant using a score for their work attribute contributions score and their
     * social attribute likeness score. The scores are weighted 90% to 10% respectively, though it is arbitrary.
     *
     * @param jsonApplicant the specified applicant being scored.
     * @return a double, the final score for the applicant.
     */
    private double score(JSONObject jsonApplicant) {
        double workAttributeScore = calculateWorkAttributeScore(jsonApplicant);
        double socialAttributeScore = calculateSocialAttributeScore(jsonApplicant);
        double workWeight = .9;
        double socialWeight = 1 - workWeight;
        return ((workWeight * workAttributeScore) + (socialWeight * socialAttributeScore));
    }

    /**
     * Calculates a score based on how well the applicant rounds out the team's work attributes
     * by comparing the highest and lowest work attribute scores after the addition of the applicant's scores.
     *
     * @param jsonApplicant the applicant being scored.
     * @return a double, the work score.
     */
    private double calculateWorkAttributeScore(JSONObject jsonApplicant) {
        JSONObject applicantAttributes = (JSONObject) jsonApplicant.get("attributes");
        // workScore will be the average of all the subscores for all work attributes
        double workScore = 0;
        double maximumAttributeSum = 0;
        double minimumAttributeSum = 1000000;
        for (String attribute : workAttributes.keySet()) {
            double teamScore = workAttributes.get(attribute);
            double applicantScore = (Long) applicantAttributes.get(attribute);
            double scoreSum = teamScore + applicantScore;
            if (scoreSum > maximumAttributeSum) {
                maximumAttributeSum = scoreSum;
            }
            if (scoreSum < minimumAttributeSum) {
                minimumAttributeSum = scoreSum;
            }
        }
        double scoreRange = maximumAttributeSum - minimumAttributeSum;
        // To get on a scale from 0-1 divide by the number of people being accounted for (team + 1) multiplied by ten
        workScore = 1 - (scoreRange / ((teamSize + 1) * 10));
        return workScore;
    }

    /**
     * Calculates a score based on how well the applicant matches the team's social attributes
     * by comparing the each of the applicant's social attribute scores, with the average score for the team in
     * that attribute.
     *
     * @param jsonApplicant the applicant being scored
     * @return a double, the social score.
     */
    private double calculateSocialAttributeScore(JSONObject jsonApplicant) {
        JSONObject applicantAttributes = (JSONObject) jsonApplicant.get("attributes");
        // socialScore will be the average of all the subscores for all social attributes
        double socialScore = 0;
        for (String attribute : socialAttributes.keySet()) {
            // subscores are calculated by formula: 1 - (abs(applicantScore - averageScore)/10).
            // This creates a score on the range of 0-1.
            double applicantAttributeScore = (Long) applicantAttributes.get(attribute);
            double difference = abs(applicantAttributeScore - socialAttributes.get(attribute));
            double subscore = 1 - (difference / 10);
            socialScore += subscore;
        }
        socialScore /= socialAttributes.size();
        return socialScore;
    }
}
