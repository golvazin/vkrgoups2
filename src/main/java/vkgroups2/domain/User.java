package vkgroups2.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "users")
public class User implements Serializable {
  
    private static final long serialVersionUID = -891118776429090996L;
    
    
    @JsonProperty("id")
    @Id
    @Column(name="id")
    private String vkId;
    
    @JsonProperty("first_name")
    @Column
    private String name;
    
    @JsonProperty("last_name")
    @Column
    private String surname;
    

    
    public User() {
    }

    public User(String vkId, String name, String surname) {
        this.vkId = vkId;
        this.name = name;
        this.surname = surname;
    }


    
    @JsonProperty("id")
    public String getVkId() {
        return vkId;
    }
    @JsonProperty("id")
    public void setVkId(String vkId) {
        this.vkId = vkId;
    }

    
    @JsonProperty("first_name")
    public String getName() {
        return name;
    }
    @JsonProperty("first_name")
    public void setName(String name) {
        this.name = name;
    }
    @JsonProperty("last_name")
    public String getSurname() {
        return surname;
    }
    @JsonProperty("last_name")
    public void setSurname(String surname) {
        this.surname = surname;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((vkId == null) ? 0 : vkId.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((surname == null) ? 0 : surname.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (vkId == null) {
            if (other.vkId != null)
                return false;
        } else if (!vkId.equals(other.vkId))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (surname == null) {
            if (other.surname != null)
                return false;
        } else if (!surname.equals(other.surname))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [id=" + vkId + ", name=" + name + ", surname="
                + surname + "]";
    }
    
    
    
    

}
