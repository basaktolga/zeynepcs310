package edu.sabanciuniv.howudoin;

import java.util.List;

public class GroupDto {
    private String name;
    private List<String> memberUsernames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMemberUsernames() {
        return memberUsernames;
    }

    public void setMemberUsernames(List<String> memberUsernames) {
        this.memberUsernames = memberUsernames;
    }
}