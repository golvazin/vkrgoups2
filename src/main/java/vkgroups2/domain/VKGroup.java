package vkgroups2.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
public class VKGroup implements Serializable {
    
    private static final long serialVersionUID = 4869448956945836700L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String url;
    
    @Column(name = "total_messages")
    private Integer totalMessages = 0;


    
    
    public VKGroup() {
    }


    public VKGroup(String name, String url) {
        this.name = name;
        this.url = url;
    }
    
    
    
    public VKGroup(String name, String url, Integer totalMessages) {
        this.name = name;
        this.url = url;
        this.totalMessages = totalMessages;
    }


    public VKGroup(Long id, String name, String url, Integer totalMessages) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.totalMessages = totalMessages;
    }


    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public String getUrl() {
        return url;
    }



    public void setUrl(String url) {
        this.url = url;
    }


  public Integer getTotalMessages() {
        return totalMessages;
    }


    public void setTotalMessages(Integer totalMessages) {
        this.totalMessages = totalMessages;
    }
    
    


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((totalMessages == null) ? 0 : totalMessages.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
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
        VKGroup other = (VKGroup) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (totalMessages == null) {
            if (other.totalMessages != null)
                return false;
        } else if (!totalMessages.equals(other.totalMessages))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "VKGroup [name=" + name + ", url=" + url + ", totalMessages=" + totalMessages + "]";
    }



}
