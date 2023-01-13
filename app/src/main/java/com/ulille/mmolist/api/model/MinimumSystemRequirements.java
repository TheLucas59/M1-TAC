package com.ulille.mmolist.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Represent the object MinimumSystemRequirements as sent by the API
 */
@Generated("jsonschema2pojo")
public class MinimumSystemRequirements {

    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("processor")
    @Expose
    private String processor;
    @SerializedName("memory")
    @Expose
    private String memory;
    @SerializedName("graphics")
    @Expose
    private String graphics;
    @SerializedName("storage")
    @Expose
    private String storage;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getGraphics() {
        return graphics;
    }

    public void setGraphics(String graphics) {
        this.graphics = graphics;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    /**
     * Check if attributes are not null, if they are they are not displayed
     * @return the MinimumRequirement as a string
     */
    @Override
    public String toString() {
        StringBuilder strb = new StringBuilder();
        if(os != null)
            strb.append(os + "\n");
        if(processor != null)
            strb.append(processor + "\n");
        if(memory != null)
            strb.append(memory + "\n");
        if(graphics != null)
            strb.append(graphics + "\n");
        if(storage != null)
            strb.append(storage + "\n");
        if(strb.length() == 0)
            return "Pas d'informations";
        return strb.toString();
    }
}