package fr.loxydev.ttcplugin.database;

import fr.loxydev.ttcplugin.team.Team;

public class TeamDataHandler extends DataHandler {

    Team team;

    public TeamDataHandler(Team team) {
        this.team = team;
        this.collectionName = team.getName();

        this.collection = getCollection("teams");
        this.nameField = "name";
        update();
    }

    public TeamDataHandler(String teamName) {
        this.collectionName = teamName;

        this.collection = getCollection("teams");
        this.nameField = "name";
        update();
    }

    // Followings methods are used to access data of a team
    public String getTeamName() {
        return data.getString("name");
    }

    // Following methods are used to set data
    public void setTeamName() {
        if (team == null) return;

        pushUpdate("name", team.getName());
    }

    public void setTeamName(String teamName)
    {
        this.collectionName = teamName;

        pushUpdate("name", teamName);
    }
}
