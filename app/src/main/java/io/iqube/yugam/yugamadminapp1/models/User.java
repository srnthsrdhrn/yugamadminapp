package io.iqube.yugam.yugamadminapp1.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("managed_events")
    @Expose
    private RealmList<ManagedEvent> managedEvents = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("authority")
    @Expose
    private String authority;
    @SerializedName("yugamid")
    @Expose
    @PrimaryKey
    private Integer yugamid;
    @SerializedName("managed_workshops")
    @Expose
    private RealmList<ManagedWorkshop> managedWorkshops = null;
    @SerializedName("email")
    @Expose
    private String email;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ManagedEvent> getManagedEvents() {
        return managedEvents;
    }

    public void setManagedEvents(RealmList<ManagedEvent> managedEvents) {
        this.managedEvents = managedEvents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Integer getYugamid() {
        return yugamid;
    }

    public void setYugamid(Integer yugamid) {
        this.yugamid = yugamid;
    }

    public List<ManagedWorkshop> getManagedWorkshops() {
        return managedWorkshops;
    }

    public void setManagedWorkshops(RealmList<ManagedWorkshop> managedWorkshops) {
        this.managedWorkshops = managedWorkshops;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}