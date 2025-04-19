package ticket_booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket_booking.entities.Train;
import ticket_booking.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TrainService {
    private Train train;
    private static List<Train>trainList;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String TRAIN_PATH = "app/src/main/java/ticket_booking/localDb/trains.json";

    public TrainService(Train train1) throws IOException {
        //constructor
        File trains = new File(TRAIN_PATH);
        trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
    }
    public static void searchTrains(String source, String destination){
        return trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());

    }
    public void addTrain{
    }
    }
