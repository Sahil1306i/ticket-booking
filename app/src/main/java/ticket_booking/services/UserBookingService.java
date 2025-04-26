package ticket_booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ticket_booking.entities.Ticket;
import ticket_booking.entities.Train;
import ticket_booking.entities.User;
import ticket_booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class UserBookingService {
    // we will import the user we took while login instead of asking the user again and again
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<User> userList;

    // when we are putting the user in the json then we will have to deserialize it and then put it.
    //so object mapper is used for mapping the user data into this
    private User user;

    private static final String USERS_PATH = "app/src/main/java/ticket_booking/localDb/users.json";

    public UserBookingService(User user1) throws IOException {
        //constructor
        this.user = user1;
        loadUsers();;

        // user is put in this on the run time
        //type refrence is wrapper which is telling the list to resolve this
    }

    // we search the user then filter the one with the name we want from the global user then

    public UserBookingService() throws IOException{
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        loadUsers();
    }

    private void loadUsers() throws IOException{
        userList = objectMapper.readValue(new File(USERS_PATH), new TypeReference<List<User>>() {});
    }
        // user is put in this on the run time
        //type refrence is wrapper which is telling the list to resolve this
        public boolean signUp(User user) throws IOException{
            try{
                Optional<User> foundUser = userList.stream().filter(user1 -> {
                    return user1.getName().equals(user.getName());
                }).findFirst();

                if (foundUser.isPresent()) {
                    // If a user with the same username exists,this will print an error message
                    System.out.println("Username already taken!");
                    return false;
                }

                userList.add(user);
                saveUserListToFile();
            }catch (Exception ex){
                System.out.println("saving user list to file failed " + ex.getMessage());
                return false;
            }
            return true;
        }
        public Optional<User> getUserByUsername(String username){
        return userList.stream().filter(user -> user.getName().equals(username)).findFirst();
    }


    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            //optional is used here so that if that user doesnot exixt then there will not be a null pointer
            return user1.getName().equalsIgnoreCase(user.getName()) &&
                    UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        // equal ignore case ignore the letter casing for the words and the letters #somewhat helpful for the dumb people out there
        // get the password from the user then match it to the hashedpassword set if that sheet matches then it returns TRUE

        return foundUser.isPresent();
        //is present is the method which will return if the user is there or not
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

    public void fetchBooking(){
        Optional<User> userFetched = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if(userFetched.isPresent()){
            userFetched.get().printTickets();
        }
    }
    public Optional<User> getUserByname(String name){
        return userList.stream().filter(user -> user.getName().equals(name)).findFirst();
    }

    public void setUser(User user){
        this.user = user;
    }

    public boolean cancelBooking(String ticketId) throws IOException{
        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }
        boolean isRemoved =  user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId) );
        if(isRemoved) {
            saveUserListToFile();
            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
            return true;
        }else{
            System.out.println("No ticket found with ID " + ticketId);
            return false;
        }
    }
    public List<Train> getTrains (String source, String destination) throws IOException {
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source,destination);
        }catch (Exception ex){
            System.out.println("There is something wrong!");
            // return empty list if there is an exception
            return Collections.emptyList();
        }
    }
    public Boolean bookTrainSeat(Train train, int row, int seat) {
        try{
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);

                    train.setSeats(seats);
                    trainService.addTrain(train);

                    Ticket ticket = new Ticket();

                    ticket.setSource(train.getStations().getFirst());
                    ticket.setDestination(train.getStations().getLast());
                    ticket.setTrain(train);
                    ticket.setUserId(user.getUserId());
                    ticket.setDateOfTravel("2021-09-01");
                    ticket.setTicketId(UserServiceUtil.generateTicketId());

                    user.getTicketsBooked().add(ticket);

                    System.out.println("Seat booked successfully  !  ");

                    System.out.println(ticket.getTicketInfo());

                    saveUserListToFile();
                    return true; // Booking successful
                } else {
                    return false; // Execute when Seat is already booked
                }
            } else {
                return false; // Execute when Invalid row or seat index
            }
        }catch (IOException ex){
            return Boolean.FALSE;
        }


    }
    public List<List<Integer>> fetchSeats(Train train){
        return train.getSeats();
    }

}

