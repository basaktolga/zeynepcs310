package edu.sabanciuniv.howudoin;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "groups")
public class Group {
    @Id
    private String id;
    private String name;
    private List<String> memberUsernames;

    public Group() {}

    public Group(String name, List<String> memberUsernames) {
        this.name = name;
        this.memberUsernames = memberUsernames;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getMemberUsernames() {
        return memberUsernames;
    }

    public void addMember(String username) {
        if (!memberUsernames.contains(username)) {
            memberUsernames.add(username);
        }
    }
}