package com.example;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.text.html.HTML;

import sun.net.www.http.HttpClient;

/**
 * Created by xklem on 13. 03. 2017.
 */

public class DataAll {
    public static final String LOKACIJA_ID = "lokacija_id";
    //http://stackoverflow.com/questions/4772425/change-date-format-in-a-java-string
    private TagList tags;
    private User userMe;
    private ArrayList<Lokacija> lokacijaList;
    private ArrayList<LokacijaTag> lokacijaTagList;

    public DataAll() {
        userMe = new User("neznani.nedolocen@nekje.ne","NiDefiniran");

        lokacijaList = new ArrayList<>();
        lokacijaTagList = new ArrayList<>();
    }
    public void addLocation(Lokacija l) {
        lokacijaList.add(l);
    }

    public Lokacija addLocation(String id,String dom, String soba, String idUser,String datum, String opis, String im, String tipNapake, Double smerX, Double smerY, String koncano) {
        Lokacija tmp = new Lokacija(id, dom, soba, idUser, datum, opis, im, tipNapake, smerX, smerY, koncano);
        lokacijaList.add(tmp);
        return tmp;
    }


    public User getUserMe() {
        return userMe;
    }

    public Lokacija getLocationByID(String ID) {
        for (Lokacija l: lokacijaList) { //TODO this solution is relatively slow! If possible don't use it!
            // if (l.getId() == ID) return l; //NAPAKA primerja reference
            if (l.getId().equals(ID)) return l;
        }
        return null;
    }

    public void addLocationTag(Lokacija l, Tag t) {
        lokacijaTagList.add(new LokacijaTag(l.id, t, System.currentTimeMillis(),userMe.getIdUser()));
    }

    @Override
    public String toString() {
        return "DataAll{" +
                "\ntags=" + tags +
                ", \nuserMe=" + userMe +
                ", \nlokacijaList=" + lokacijaList +
                ", \nlokacijaTagList=" + lokacijaTagList +
                '}';
    }


    public static DataAll scenarijA(String dom) {
        DataAll da = new DataAll();
        Date danes = new Date();
        da.userMe = new User("xklemenx@gmail.com","Klemen Andrejc Kac");
        Lokacija tmp;

        return da;
    }

    public void addLokacija(Lokacija l) {
        lokacijaList.add(l);
    }

    public Lokacija getLocation(int i) {
        return lokacijaList.get(i);
    }

    public LokacijaTag getLocationTag(int i) {
        return lokacijaTagList.get(i);
    }

    public int getLocationSize() {
        return lokacijaList.size();
    }

    public List<Lokacija> getLokacijaAll() {
        return lokacijaList;
    }


    public void addNewLocationTag(LokacijaTag tag) {
        lokacijaTagList.add(tag);
    }
    public void addNewLocationTags(ArrayList<LokacijaTag> tags) {
        lokacijaTagList.addAll(tags);
    }

    public ArrayList<LokacijaTag> getTagList(String locationId) {
        ArrayList<LokacijaTag> tags = new ArrayList<>();
        for (LokacijaTag lt:lokacijaTagList) {
            if (lt.getIdLokacija().equals(locationId)) {
                tags.add(lt);
            }
        }
        return  tags;
    }

    public void removeFromTagList(String locationId) {
        for (int i=lokacijaTagList.size()-1;i>=0; i--) {
            if (lokacijaTagList.get(i).getIdLokacija().equals(locationId))
                lokacijaTagList.remove(i);
        }
    }

    public void removeLocation(String locationId) {
        for(int i=0;i<getLokacijaAll().size();i++){
            if(getLocation(i).getId().equals(locationId)){
                getLokacijaAll().remove(i);
            }
        }

    }

    public ArrayList<LokacijaTag> getDefultTagLists(ArrayList<Tag> tags, Lokacija l) {
        ArrayList<LokacijaTag> lt = new ArrayList<>();

        for (Tag t:tags){
            lt.add(new LokacijaTag(l.getId(),t,System.currentTimeMillis(),userMe.getIdUser()));
        }
        return lt;
    }
}
