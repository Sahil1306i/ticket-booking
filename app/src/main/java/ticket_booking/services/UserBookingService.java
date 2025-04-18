package ticket_booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket_booking.entities.Ticket;
import ticket_booking.entities.User;
import ticket_booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
    // we search the user then filter the one with the name we want from the global user then

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            //optional is used here so that if that user doesnot exixt then there will not be a null pointer
            return user1.getName().equalsIgnoreCase(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        // equal ignore case ignore the letter casing for the words and the letters #somewhat helpful for the dumb people out there
        // get the password from the user then match it to the hashedpassword set if that sheet matches then it returns TRUE

        return foundUser.isPresent();
        //is present is the method which will return if the user is there or not
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }
        catch (IOException ex){
            return Boolean.FALSE;
        }
    }
    //this thing will save the user to the local DB # we have added the user to the global list but not in the local db
    private void saveUserListToFile() throws IOException{
        File usersFile = new File(USERS_PATH);
        // this thing is calling the userJSON file
        // serialization is happening here
        objectMapper.writeValue(usersFile, userList);
    }

    //json --> Object(User) --> deserialization
    //Object --> json --> serialize

    public void fetchBooking{
        Optional<User> userFetched = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if(userFetched.isPresent()){
            userFetched.get().printTickets();
        }
    }
    public Boolean cancelBooking(String ticketId){
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the ticketID to be cancelled");
        ticketId = s.next();

        if(ticketId == null || ticketId.isEmpty()){
            System.out.println("ticketId cannot be null or empty");
        }
        String finalTicketId1 = ticketId;  // because strings are immutable
        boolean removed  = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId1));

        String finalTicketId = ticketId;
        user.getTicketsBooked().removeIf(ticket -> Ticket.getTicketId().equals(finalTicketId));
        if(removed){
            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
        }else{
            System.out.println("No ticket with this TicketId found");
        }
        return Boolean.FALSE;
    }
}