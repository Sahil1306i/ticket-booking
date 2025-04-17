package ticket_booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket_booking.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;
    // we will import the user we took while login instead of asking the user again and again

    private List<User> userList;

    private ObjectMapper objectMapper = new ObjectMapper();
    // when we are putting the user in the json then we will have to deserialize it and then put it.
    //so object mapper is used for mapping the user data into this

    private static final String USERS_PATH = "app/src/main/java/ticket_booking/localDb/users.json";

    public UserBookingService(User user1) throws IOException {
        //constructor

        this.user = user1;
        //taking the user globally
        File users =new File(USERS_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>(){});
        // user is put in this on the run time
        //type refrence is wrapper which is telling the list to resolve this
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }


}
