package com.example.hikelowna.core;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Trail implements Comparable<Trail>, Serializable {
    String separator = " | ";
    private String name;
    private String difficulty;
    private float length;
    private float estimatedTime;
    private float rating;
    private LatLng latLng;

    // Default constructor
    public Trail() {
        this.name = "";
        this.difficulty = "";
        this.length = -1.00f;
        this.estimatedTime = -1.00f;
        this.rating = 0.0f;
    }

    // Full constructor
    public Trail(String name, String difficulty, float length, float estimatedTime, float rating) {
        this.name = name;
        this.difficulty = difficulty;
        this.length = length;
        this.estimatedTime = estimatedTime;
        this.rating = rating;
    }

    public static List<LatLng> getTrailPoints(Trail trail) {
        List<LatLng> trailPoints = new ArrayList<>();
        if (trail.getName().equals("Apex Trail")) {
            trailPoints.add(new LatLng(49.9053217, -119.4903617));
            trailPoints.add(new LatLng(49.9053409, -119.4903121));
            trailPoints.add(new LatLng(49.9053401, -119.4902718));
            trailPoints.add(new LatLng(49.9053336, -119.4902262));
            trailPoints.add(new LatLng(49.9053092, -119.4901189));
            trailPoints.add(new LatLng(49.9052535, -119.4899811));
            trailPoints.add(new LatLng(49.9052222, -119.489844));
            trailPoints.add(new LatLng(49.9052019, -119.4896546));
            trailPoints.add(new LatLng(49.9051889, -119.4894863));
            trailPoints.add(new LatLng(49.9052086, -119.4893605));
            trailPoints.add(new LatLng(49.9052507, -119.4892975));
            trailPoints.add(new LatLng(49.9053064, -119.4892606));
            trailPoints.add(new LatLng(49.9053386, -119.4892606));
            trailPoints.add(new LatLng(49.9054226, -119.4894534));
            trailPoints.add(new LatLng(49.9055156, -119.4896663));
            trailPoints.add(new LatLng(49.9055513, -119.4896921));
            trailPoints.add(new LatLng(49.9055832, -119.4896985));
            trailPoints.add(new LatLng(49.9056191, -119.4896884));
            trailPoints.add(new LatLng(49.9056383, -119.4896429));
            trailPoints.add(new LatLng(49.9056301, -119.4895862));
            trailPoints.add(new LatLng(49.9056085, -119.489504));
            trailPoints.add(new LatLng(49.9056076, -119.4894668));
            trailPoints.add(new LatLng(49.905597, -119.4893827));
            trailPoints.add(new LatLng(49.9055919, -119.4893143));
            trailPoints.add(new LatLng(49.905591, -119.4892553));
            trailPoints.add(new LatLng(49.9055977, -119.489216));
            trailPoints.add(new LatLng(49.9056182, -119.4892103));
            trailPoints.add(new LatLng(49.905653, -119.489213));
            trailPoints.add(new LatLng(49.9056931, -119.4892492));
            trailPoints.add(new LatLng(49.9057095, -119.4893009));
            trailPoints.add(new LatLng(49.9057633, -119.4894192));
            trailPoints.add(new LatLng(49.9057868, -119.489444));
            trailPoints.add(new LatLng(49.9058335, -119.4894638));
            trailPoints.add(new LatLng(49.9058559, -119.4894658));
            trailPoints.add(new LatLng(49.9058775, -119.489447));
            trailPoints.add(new LatLng(49.9058814, -119.4893884));
            trailPoints.add(new LatLng(49.9058806, -119.489329));
            trailPoints.add(new LatLng(49.9058749, -119.4892667));
            trailPoints.add(new LatLng(49.9058726, -119.4892321));
            trailPoints.add(new LatLng(49.905867, -119.4892026));
            trailPoints.add(new LatLng(49.9058473, -119.4891081));
            trailPoints.add(new LatLng(49.9058361, -119.4889676));
            trailPoints.add(new LatLng(49.9058303, -119.488922));
            trailPoints.add(new LatLng(49.9058274, -119.4888704));
            trailPoints.add(new LatLng(49.9058419, -119.4888003));
            trailPoints.add(new LatLng(49.9058654, -119.4887604));
            trailPoints.add(new LatLng(49.9058873, -119.4887601));
            trailPoints.add(new LatLng(49.9059142, -119.4887819));
            trailPoints.add(new LatLng(49.9059458, -119.4888204));
            trailPoints.add(new LatLng(49.9060082, -119.4888989));
            trailPoints.add(new LatLng(49.9060611, -119.4889599));
            trailPoints.add(new LatLng(49.9060863, -119.4889877));
            trailPoints.add(new LatLng(49.9061339, -119.4890139));
            trailPoints.add(new LatLng(49.9061842, -119.4890333));
            trailPoints.add(new LatLng(49.9063213, -119.4890893));
            trailPoints.add(new LatLng(49.9063839, -119.4891155));
            trailPoints.add(new LatLng(49.9064042, -119.4891242));
            trailPoints.add(new LatLng(49.9064411, -119.4891875));
            trailPoints.add(new LatLng(49.9065016, -119.4893086));
            trailPoints.add(new LatLng(49.9065212, -119.489331));
            trailPoints.add(new LatLng(49.9066221, -119.4893388));
            trailPoints.add(new LatLng(49.9067309, -119.4893803));
            trailPoints.add(new LatLng(49.9068296, -119.4894212));
            trailPoints.add(new LatLng(49.907085, -119.489499));
            trailPoints.add(new LatLng(49.90735, -119.4895802));
            trailPoints.add(new LatLng(49.9074238, -119.4895916));
            trailPoints.add(new LatLng(49.9074653, -119.489615));
            trailPoints.add(new LatLng(49.9075002, -119.489668));
            trailPoints.add(new LatLng(49.9076242, -119.489835));
            trailPoints.add(new LatLng(49.9077028, -119.4899617));
            trailPoints.add(new LatLng(49.907733, -119.4900375));
            trailPoints.add(new LatLng(49.9077775, -119.4901588));
            trailPoints.add(new LatLng(49.9078181, -119.4903157));
            trailPoints.add(new LatLng(49.9078565, -119.4903801));
            trailPoints.add(new LatLng(49.9079191, -119.4904318));
            trailPoints.add(new LatLng(49.9079744, -119.4904934));
            trailPoints.add(new LatLng(49.9080107, -119.4905746));
            trailPoints.add(new LatLng(49.9080392, -119.4906732));
            trailPoints.add(new LatLng(49.9080694, -119.490759));
            trailPoints.add(new LatLng(49.9080832, -119.4907831));
            trailPoints.add(new LatLng(49.908186, -119.4908408));
            trailPoints.add(new LatLng(49.9081961, -119.4908412));
            trailPoints.add(new LatLng(49.9082393, -119.4909619));
            trailPoints.add(new LatLng(49.9082812, -119.4910504));
            trailPoints.add(new LatLng(49.9083188, -119.4911094));
            trailPoints.add(new LatLng(49.9083624, -119.4911597));
            trailPoints.add(new LatLng(49.9084172, -119.4911617));
            trailPoints.add(new LatLng(49.9084639, -119.4911503));
            trailPoints.add(new LatLng(49.9084928, -119.4911221));
            trailPoints.add(new LatLng(49.9085006, -119.4911456));
            trailPoints.add(new LatLng(49.908542, -119.4911999));
            trailPoints.add(new LatLng(49.9085623, -119.491224));
            trailPoints.add(new LatLng(49.9085999, -119.4912395));
            trailPoints.add(new LatLng(49.9086945, -119.4912777));
            trailPoints.add(new LatLng(49.9087485, -119.4912985));
            trailPoints.add(new LatLng(49.9088184, -119.4913166));
            trailPoints.add(new LatLng(49.908853, -119.4913219));
            trailPoints.add(new LatLng(49.9088711, -119.491338));
            trailPoints.add(new LatLng(49.9088901, -119.4913468));
            trailPoints.add(new LatLng(49.9089044, -119.4913642));
            trailPoints.add(new LatLng(49.9089212, -119.4913803));
            trailPoints.add(new LatLng(49.9089346, -119.4914011));
            trailPoints.add(new LatLng(49.9089432, -119.4914212));
            trailPoints.add(new LatLng(49.9089588, -119.4914668));
            trailPoints.add(new LatLng(49.9089855, -119.4915023));
            trailPoints.add(new LatLng(49.9090119, -119.4915412));
            trailPoints.add(new LatLng(49.9090343, -119.4915734));
            trailPoints.add(new LatLng(49.9090473, -119.4916076));
            trailPoints.add(new LatLng(49.9090844, -119.4916264));
            trailPoints.add(new LatLng(49.9091116, -119.4916311));
            trailPoints.add(new LatLng(49.9091293, -119.4916358));
            trailPoints.add(new LatLng(49.9091557, -119.4916277));
            trailPoints.add(new LatLng(49.9091829, -119.4916056));
            trailPoints.add(new LatLng(49.9091959, -119.4915835));
            trailPoints.add(new LatLng(49.9091924, -119.4915352));
            trailPoints.add(new LatLng(49.9091816, -119.4915003));
            trailPoints.add(new LatLng(49.9091626, -119.4914695));
            trailPoints.add(new LatLng(49.9091423, -119.49145));
            trailPoints.add(new LatLng(49.9091164, -119.4914192));
            trailPoints.add(new LatLng(49.9090961, -119.4913823));
            trailPoints.add(new LatLng(49.9090749, -119.4913427));
            trailPoints.add(new LatLng(49.9090607, -119.4913119));
            trailPoints.add(new LatLng(49.9090417, -119.4912743));
            trailPoints.add(new LatLng(49.9090205, -119.4912428));
            trailPoints.add(new LatLng(49.9090007, -119.4912227));
            trailPoints.add(new LatLng(49.9088983, -119.4911194));
            trailPoints.add(new LatLng(49.9088508, -119.4910591));
            trailPoints.add(new LatLng(49.9088387, -119.4910423));
            trailPoints.add(new LatLng(49.9088318, -119.4910236));
            trailPoints.add(new LatLng(49.9088348, -119.4910081));
            trailPoints.add(new LatLng(49.9088435, -119.4909994));
            trailPoints.add(new LatLng(49.9088495, -119.4909981));
            trailPoints.add(new LatLng(49.9089022, -119.4910209));
            trailPoints.add(new LatLng(49.9089393, -119.4910343));
            trailPoints.add(new LatLng(49.9089609, -119.491043));
            trailPoints.add(new LatLng(49.9090352, -119.4910376));
            trailPoints.add(new LatLng(49.9090888, -119.4910276));
            trailPoints.add(new LatLng(49.9091509, -119.4910021));
            trailPoints.add(new LatLng(49.9092153, -119.4909639));
            trailPoints.add(new LatLng(49.9092978, -119.4908827));
            trailPoints.add(new LatLng(49.9093643, -119.4907982));
            trailPoints.add(new LatLng(49.9094157, -119.4907325));
            trailPoints.add(new LatLng(49.9094498, -119.4906903));
            trailPoints.add(new LatLng(49.9094541, -119.4907003));
            trailPoints.add(new LatLng(49.9094757, -119.4907828));
            trailPoints.add(new LatLng(49.9094921, -119.4908257));
            trailPoints.add(new LatLng(49.909534, -119.4908733));
            trailPoints.add(new LatLng(49.9095633, -119.4908988));
            trailPoints.add(new LatLng(49.9096026, -119.4909189));
            trailPoints.add(new LatLng(49.9096454, -119.4909243));
            trailPoints.add(new LatLng(49.9096769, -119.4909203));
            trailPoints.add(new LatLng(49.9097076, -119.4909102));
            trailPoints.add(new LatLng(49.9097356, -119.4908868));
            trailPoints.add(new LatLng(49.9098026, -119.490821));
            trailPoints.add(new LatLng(49.9099071, -119.4907198));
            trailPoints.add(new LatLng(49.9099459, -119.4907084));
            trailPoints.add(new LatLng(49.9099805, -119.4907205));
            trailPoints.add(new LatLng(49.9100055, -119.4907399));
            trailPoints.add(new LatLng(49.9100159, -119.4907627));
            trailPoints.add(new LatLng(49.9100202, -119.490811));
            trailPoints.add(new LatLng(49.9100155, -119.4908492));
            trailPoints.add(new LatLng(49.9099852, -119.4909196));
            trailPoints.add(new LatLng(49.9099442, -119.4910008));
            trailPoints.add(new LatLng(49.909892, -119.491098));
            trailPoints.add(new LatLng(49.9098686, -119.4911483));
            trailPoints.add(new LatLng(49.9098626, -119.4911898));
            trailPoints.add(new LatLng(49.9098591, -119.4912442));
            trailPoints.add(new LatLng(49.9098376, -119.4912777));
            trailPoints.add(new LatLng(49.9097974, -119.491324));
            trailPoints.add(new LatLng(49.9097736, -119.4913501));
            trailPoints.add(new LatLng(49.9097568, -119.4913964));
            trailPoints.add(new LatLng(49.9097473, -119.4914768));
            trailPoints.add(new LatLng(49.9097482, -119.4915177));
            trailPoints.add(new LatLng(49.9097525, -119.4915439));
            trailPoints.add(new LatLng(49.9097646, -119.4915654));
            trailPoints.add(new LatLng(49.9098147, -119.4916036));
            trailPoints.add(new LatLng(49.9098682, -119.4916398));
            trailPoints.add(new LatLng(49.909933, -119.4916887));
            trailPoints.add(new LatLng(49.909974, -119.4917149));
            trailPoints.add(new LatLng(49.9099926, -119.4917182));
            trailPoints.add(new LatLng(49.9100176, -119.4917169));
            trailPoints.add(new LatLng(49.9100608, -119.4917015));
            trailPoints.add(new LatLng(49.9101139, -119.4916861));
            trailPoints.add(new LatLng(49.9101485, -119.4916706));
            trailPoints.add(new LatLng(49.9101839, -119.4916391));
            trailPoints.add(new LatLng(49.9102029, -119.4916177));
            trailPoints.add(new LatLng(49.9102318, -119.4915513));
            trailPoints.add(new LatLng(49.9102629, -119.4914802));
            trailPoints.add(new LatLng(49.9104145, -119.4911382));
            trailPoints.add(new LatLng(49.910424, -119.4911315));
            trailPoints.add(new LatLng(49.9104365, -119.4911369));
            trailPoints.add(new LatLng(49.9104408, -119.4911496));
            trailPoints.add(new LatLng(49.91044, -119.4911744));
            trailPoints.add(new LatLng(49.9104356, -119.4911912));
            trailPoints.add(new LatLng(49.9104218, -119.491222));
            trailPoints.add(new LatLng(49.9104106, -119.4912509));
            trailPoints.add(new LatLng(49.9104002, -119.4912824));
            trailPoints.add(new LatLng(49.9103946, -119.4913119));
            trailPoints.add(new LatLng(49.9103868, -119.4913595));
            trailPoints.add(new LatLng(49.9103834, -119.4913977));
            trailPoints.add(new LatLng(49.9103804, -119.491452));
            trailPoints.add(new LatLng(49.9103756, -119.4915714));
            trailPoints.add(new LatLng(49.9103622, -119.4919));
            trailPoints.add(new LatLng(49.9103566, -119.491967));
            trailPoints.add(new LatLng(49.9103462, -119.492024));
            trailPoints.add(new LatLng(49.9103393, -119.4920602));
            trailPoints.add(new LatLng(49.9103234, -119.4921031));
            trailPoints.add(new LatLng(49.9103057, -119.492138));
            trailPoints.add(new LatLng(49.9102776, -119.4921789));
            trailPoints.add(new LatLng(49.9102474, -119.4922245));
            trailPoints.add(new LatLng(49.9102145, -119.4922708));
            trailPoints.add(new LatLng(49.9101735, -119.4923298));
            trailPoints.add(new LatLng(49.9101372, -119.4923834));
            trailPoints.add(new LatLng(49.9101122, -119.4924176));
            trailPoints.add(new LatLng(49.9100932, -119.4924451));
            trailPoints.add(new LatLng(49.9100859, -119.4924579));

            return trailPoints;
        }
        return trailPoints;
    }

    @Override
    public int compareTo(Trail other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    // Method to generate difficulty stars
    public String getDifficultyStars() {
        String stars = "⬦⬦⬦⬦⬦";
        if (difficulty.equalsIgnoreCase("easy")) {
            stars = "⬥⬦⬦⬦⬦";
        } else if (difficulty.equalsIgnoreCase("moderate")) {
            stars = "⬥⬥⬦⬦⬦";
        } else if (difficulty.equalsIgnoreCase("difficult")) {
            stars = "⬥⬥⬥⬦⬦";
        } else if (difficulty.equalsIgnoreCase("extreme")) {
            stars = "⬥⬥⬥⬥⬦";
        } else if (difficulty.equalsIgnoreCase("impossible")) {
            stars = "⬥⬥⬥⬥⬥";
        }
        return stars;
    }

    // Method to generate rating stars
    public String getRatingStars() {
        return "★ " + this.rating;
    }

    // Shortened toString for list view
    public String toStringShort() {
        return getRatingStars() + separator
                + getDifficultyStars() + separator
                + length + "k" + separator
                + estimatedTime + "h";
    }

    // Full toString for detailed view
    @Override
    public String toString() {
        return name + "\n"
                + getRatingStars() + separator + getDifficultyStars() + "\n"
                + length + "k" + separator + estimatedTime + "h\n";
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(float estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}