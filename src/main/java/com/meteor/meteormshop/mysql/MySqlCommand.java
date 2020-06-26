package com.meteor.meteormshop.mysql;

public enum MySqlCommand {
    //sql命令枚举
    CREATE_TABLE("create table if not exists player_data ("+
            "player varchar(20),"+
            "data LONGBLOB,"+
            "PRIMARY KEY(player))"
    ),
    CHECK_DATA("select * from player_data where player=?"),
    UPDATE_DATA("update player_data set data=? where player=?"),
    ADD_PLAYER("INSERT INTO player_data (data,player) values (?,?)");

    private String command;
    MySqlCommand(String cmd){
        this.command = cmd;
    }
    public String getCommand(){
        return this.command;
    }
}
