package com.tchnovision.bot.getAndSetCarts;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;

public class GetUserCarts {
    private User user;
    private List<Integer> carts_list = new ArrayList<Integer>();
    private String callComand = "!mycarts";
    private Long IDMessage;
    int page = 1;
    private String PATH;
    int replaceId = -1;
    String ID;
    public GetUserCarts(User user, TextChannel channel, String path, String Id){
        ID = Id;
        GetCarts(user, channel, path);}
    public SelectMenu getMenu(String string){ List<SelectOption> options = new ArrayList<>();
        try {


            Class.forName("org.sqlite.JDBC");
            try(Connection connection = DriverManager.getConnection("jdbc:sqlite:characters.db")){
                String[] strings = string.split(";");
                int i = 0;
                for (String s:strings){


                Statement st = connection.createStatement();
                String sql = "SELECT * FROM Characters Where ID = " + s;
                ResultSet rs = st.executeQuery(sql);



                        options.add(SelectOption.of(rs.getString("name"), Integer.toString(i)));
                        i++;

                }}
                SelectMenu menu = SelectMenu.create("GUCChooseRCarts/"+ID)
                        .setPlaceholder("карты")
                        .addOptions(options)
                        .build();
                return menu;

                        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

    }
    public void GetCarts(User usr, @NotNull TextChannel chanel, String path){
        PATH= path;
        user = usr;
        chanel.sendMessage(user.getAsMention() + " ваши карты:").queue();
        try {
            Class.forName("org.sqlite.JDBC");
            try(Connection connection = DriverManager.getConnection("jdbc:sqlite:characters.db")) {
                Statement st = connection.createStatement();
                String sql = "SELECT Carts FROM Users WHERE UserID = '" + user.getAsMention() + "'";
                ResultSet rs = st.executeQuery(sql);
                String str = rs.getString("Carts");
                String[] str1 = str.split(";");
                for (String i:str1){
                    carts_list.add(Integer.parseInt(i));
                }
                List<Button> buttons = new ArrayList<Button>();
                buttons.add(Button.primary("GUCPageBack/"+ID, "<"));
                buttons.add(Button.primary("GUCPageNext/"+ID, ">"));
                buttons.add(Button.danger("GUCReplace/"+ID, "Сменить"));

                String[] config = this.getConFromCharacters(carts_list.get(page)).split(";");
                String mesge = "Карта в колоде под номером: " + Integer.toString(page + 1) + "\n" +
                        "Имя: " + config[3] + "\n" +
                        "Стоимость Маны: " + config[2] + "\n" +
                        "ХП: " + config[0] + "\n" +
                        "АТК: " + config[1] + "\n" +
                        "ID: " + Integer.toString(carts_list.get(page)) + "\n"
                        ;
                Message messge = new MessageBuilder()
                        .append(mesge)

                        .setActionRows(ActionRow.of(getMenu(str)),ActionRow.of(buttons))
                        .build();
                chanel.sendMessage(messge).queue(message -> {IDMessage = message.getIdLong();});


            }
            catch (SQLException e) {
                try(Connection connection = DriverManager.getConnection("jdbc:sqlite:characters.db")) {
                    String sql = "INSERT INTO Users(userID, carts, ability) VALUES (?,?,?)";
                    PreparedStatement pstm = connection.prepareStatement(sql);
                    pstm.setString(1, user.getAsMention());
                    pstm.setString(2, "1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;26");
                    pstm.setInt(3, 0);
                    pstm.executeUpdate();
                    String str = "1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;26";
                    String[] str1 = str.split(";");
                    for (String i:str1){
                        carts_list.add(Integer.parseInt(i));
                    }
                    List<Button> buttons = new ArrayList<Button>();
                    buttons.add(Button.primary("GUCPageBack/"+ID, "<"));
                    buttons.add(Button.primary("GUCPageNext/"+ID, ">"));
                    buttons.add(Button.danger("GUCReplace/"+ID, "Сменить"));
                    String[] config = this.getConFromCharacters(carts_list.get(page)).split(";");
                    String mesge = "Карта в колоде под номером: " + Integer.toString(page + 1) + "\n" +
                            "Имя: " + config[3] + "\n" +
                            "Стоимость Маны: " + config[2] + "\n" +
                            "ХП: " + config[0] + "\n" +
                            "АТК: " + config[1] + "\n" +
                            "ID: " + Integer.toString(carts_list.get(page)) + "\n"
                            ;
                    Message messge = new MessageBuilder()
                            .append(mesge)

                            .setActionRows(ActionRow.of(getMenu(str)),ActionRow.of(buttons))
                            .build();
                    chanel.sendMessage(messge).queue(message -> {IDMessage = message.getIdLong();});

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public String getID(){return ID;}
    public String getConFromCharacters(int id){
        try {


        Class.forName("org.sqlite.JDBC");
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:characters.db")){
            Statement st = connection.createStatement();
            String sql = "SELECT * FROM Characters WHERE ID = " + Integer.toString(id);
            ResultSet rs = st.executeQuery(sql);
            String result = rs.getString("HP") + ";" + rs.getString("ATK") + ";" +
                    rs.getString("ManaCost") + ";" + rs.getString("NAME") + ";" + rs.getString("Image");
            return result;
        } catch (SQLException e) {
            return "; ; ; ;none.png; ";
        }} catch (ClassNotFoundException e) {
            return "; ; ; ;none.png; ";
        }

    }
    public void CommandListener(Long id, User usr, TextChannel channel, String command, ButtonInteractionEvent event){

        if(id.equals(IDMessage) && usr.equals(user)){
            if(command.equals("GUCPageBack")){
                page -= 1;
                if (page < 0) page += 25;
                String[] config = this.getConFromCharacters(carts_list.get(page)).split(";");
                String message = "Карта в колоде под номером: " + Integer.toString(page + 1) + "\n" +
                        "Имя: " + config[3] + "\n" +
                        "Стоимость Маны: " + config[2] + "\n" +
                        "ХП: " + config[0] + "\n" +
                        "АТК: " + config[1] + "\n" +
                        "ID: " + Integer.toString(carts_list.get(page)) + "\n"
                        ;
                event.editMessage(message + config[4]).queue();

            }
            else if(command.equals("GUCPageNext")){
                page += 1;
                if (page >= 25) page -= 25;
                String[] config = this.getConFromCharacters(carts_list.get(page)).split(";");
                String message = "Карта в колоде под номером: " + Integer.toString(page + 1) + "\n" +
                        "Имя: " + config[3] + "\n" +
                        "Стоимость Маны: " + config[2] + "\n" +
                        "ХП: " + config[0] + "\n" +
                        "АТК: " + config[1] + "\n" +
                        "ID: " + Integer.toString(carts_list.get(page)) + "\n"
                        ;
                event.editMessage(message + config[4]).queue();
            }
            else if(command.equals("GUCPageSet")){
                TextInput nomPage = TextInput.create("GUCNomPage/"+ID, "введите страницу", TextInputStyle.SHORT)
                        .setPlaceholder("1-25")
                        .setMinLength(1)
                        .setMaxLength(1000)
                        .setRequired(true)
                        .build();
                Modal modal = Modal.create("GUCPageSelect/"+ID, "выбери страницу")
                        .addActionRow(nomPage)
                        .build();
                event.replyModal(modal).queue();
            }
            else if (command.equals("GUCReplace")){
                List<SelectOption> options = new ArrayList<>();
                try {


                    Class.forName("org.sqlite.JDBC");
                    try(Connection connection = DriverManager.getConnection("jdbc:sqlite:characters.db")){
                        List<Button> buttons = new ArrayList<Button>();
                        buttons.add(Button.success("successReplace/"+ID, "поменять"));
                        buttons.add(Button.danger("delReplace/"+ID, "отмена"));
                        Statement st = connection.createStatement();
                        String sql = "SELECT * FROM Characters";
                        ResultSet rs = st.executeQuery(sql);
                        while (rs.next()){

                            if(!carts_list.contains(Integer.parseInt(rs.getString(1))) && rs.getInt("canTake") == 1){
                                options.add(SelectOption.of(rs.getString("name"), rs.getString(1)));
                            }
                        }
                        SelectMenu menu = SelectMenu.create("GUCChooseCarts/"+ID)
                                .setPlaceholder("карты")
                                .addOptions(options)
                                .build();
                        Message messag = new MessageBuilder()
                                .append("выбери карту")
                                .setActionRows(ActionRow.of(menu),ActionRow.of(buttons))

                                .build();
                        event.getInteraction().reply(messag).setEphemeral(true).queue();
                    } catch (SQLException e) {
                    }} catch (ClassNotFoundException e) {

            }
            }

        }
        else if (command.equals("delReplace")){
            Message messag = new MessageBuilder()
                    .append("отменненно")
                    .build();
            event.editMessage(messag).retainFiles().queue();
        }
        else if(command.equals("successReplace")){
            if(carts_list.contains(replaceId)) return;
            if(replaceId == -1) return;
            carts_list.set(page,replaceId);
            StringBuilder t = new StringBuilder();
            for (int i:carts_list){
                t.append(Integer.toString(i)).append(";");
            }
            String t1 = t.substring(0, t.length()-1);
            try {
                Class.forName("org.sqlite.JDBC");
                try(Connection connection = DriverManager.getConnection("jdbc:sqlite:characters.db")) {
                    String sql = "UPDATE Users SET Carts = ? WHERE UserID ="+"'" +user
                            .getAsMention() + "'";
                    PreparedStatement pstm = connection.prepareStatement(sql);
                    pstm.setString(1, t1);
                    pstm.executeUpdate();
                    event.deferEdit().queue();

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void ModalSetPage(String us, String mes, TextChannel channel, ModalInteractionEvent event){
        if(!user.getAsMention().equals(us)) return;
        int pg = -1;
        try {
            pg = Integer.parseInt(mes);
        }
        catch (NumberFormatException e){
            channel.sendMessage("ты что пишешь?").queue();
            return;
        }
        if(pg < 1 || pg >25) return;
        page = pg-1;
        String[] config = this.getConFromCharacters(carts_list.get(page)).split(";");
        String message = "Карта в колоде под номером: " + Integer.toString(page + 1) + "\n" +
                "Имя: " + config[3] + "\n" +
                "Стоимость Маны: " + config[2] + "\n" +
                "ХП: " + config[0] + "\n" +
                "АТК: " + config[1] + "\n" +
                "ID: " + Integer.toString(carts_list.get(page)) + "\n"
                ;
        event.editMessage(message + config[4]).queue();
    }
    public String getCallComand(){
        return callComand;
    }
    public void SetCart(int id, String options, SelectMenuInteractionEvent event){
        if (options.equals("GUCChooseCarts")){replaceId=id;
            String[] config = this.getConFromCharacters(id).split(";");
            String message = "Карта в колоде под номером: " + Integer.toString(page + 1) + "\n" +
                    "Имя: " + config[3] + "\n" +
                    "Стоимость Маны: " + config[2] + "\n" +
                    "ХП: " + config[0] + "\n" +
                    "АТК: " + config[1] + "\n" +
                    "ID: " + Integer.toString(carts_list.get(page)) + "\n"
                    ;
            event.editMessage(config[4]).queue();}
        else if(options.equals("GUCChooseRCarts")){
            page = Integer.parseInt(event.getValues().get(0));
            System.out.println(page);
            String[] config = this.getConFromCharacters(carts_list.get(page)).split(";");
            String message = "Карта в колоде под номером: " + Integer.toString(page + 1) + "\n" +
                    "Имя: " + config[3] + "\n" +
                    "Стоимость Маны: " + config[2] + "\n" +
                    "ХП: " + config[0] + "\n" +
                    "АТК: " + config[1] + "\n" +
                    "ID: " + Integer.toString(carts_list.get(page)) + "\n"
                    ;
            event.editMessage(message + config[4]).queue();
        }
    }

    }

