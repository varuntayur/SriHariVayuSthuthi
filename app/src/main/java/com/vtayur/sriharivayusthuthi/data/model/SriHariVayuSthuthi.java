package com.vtayur.sriharivayusthuthi.data.model;

import org.simpleframework.xml.ElementList;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vtayur on 8/19/2014.
 */
public class SriHariVayuSthuthi implements Serializable {

    @ElementList(inline = true, name = "section")
    List<Section> sections;

    private Map<String, Section> mapSecName2Sec = new LinkedHashMap<String, Section>();

    public SriHariVayuSthuthi() {
    }

    @Override
    public String toString() {
        return "SriHariVayuSthuthi{" +
                "sections=" + sections +
                '}';
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public Section getSection(String sectionName) {

        buildMap();

        return mapSecName2Sec.get(sectionName);
    }

    public Collection<String> getSectionNames() {

        buildMap();

        return mapSecName2Sec.keySet();
    }

    private void buildMap() {
        if (mapSecName2Sec.keySet().isEmpty())
            for (Section section : sections) {
                mapSecName2Sec.put(section.getName(), section);
            }
    }
}
