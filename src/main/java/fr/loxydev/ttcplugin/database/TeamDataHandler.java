package fr.loxydev.ttcplugin.database;

public class TeamDataHandler extends DataHandler {

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
    public void setTeamName(String teamName)
    {
        this.collectionName = teamName;

        pushUpdate("name", teamName);
    }
}
