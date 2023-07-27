package services;

import lombok.extern.slf4j.Slf4j;
import model.Car;
import model.CarVote;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static services.MessageService.*;

@Slf4j
public class VotingService {
    private ArrayList<CarVote> carsVotesList = new ArrayList<>();
    private MessageService messageService = new MessageService();

    public void startVotingService() {
        boolean isStarted = true;
        int count = addCarsCount();
        while (count == -1 || count > 99) {
            System.out.printf(INCORRECT_COUNT_VOTE);
            count = addCarsCount();
        }
        addCars(count);
        System.out.println(messageService.infoVoting(carsVotesList));

        while (isStarted) {
            System.out.println(CHANGE_REQUEST);
            String carText = new Scanner(System.in).nextLine();
            if (carText.equals("0")) {
                isStarted = false;
            } else {
                boolean isAddCar = isCarInVoting(carText);
                if (isAddCar) {
                    System.out.println(VOTE_ADDED);
                } else {
                    System.out.println(VOTE_ERROR);
                }
                log.info("Добавление 1 голоса за автомобиль: {} выполнено: {}", carText, isAddCar);
            }
        }

        log.info("{}", carsVotesList);
        CarVote maxCarVote = getMaxCarVote(carsVotesList);
        if (maxCarVote.getCountVotes() !=0) {
            String message = messageService.getStatistics(maxCarVote);
            System.out.println(message);
        } else {
            log.info("Голосования завершилось без принятых голосов: {}",
                    maxCarVote.getCountVotes());
            System.out.println(INCORRECT_END_VOTE);
        }
    }

    public CarVote getMaxCarVote(ArrayList<CarVote> carsVotesList) {
        CarVote maxCarVote = Collections.max(
                carsVotesList,
                (o1, o2) -> o1.getCountVotes() > o2.getCountVotes() ? 1 : -1);
        return maxCarVote;
    }

    public boolean isCarInVoting(String carText) {
        boolean isCar = false;
        String[] infoCar = arrayInfoCar(carText);
        if (infoCar.length < 4) {
            for (CarVote carVote : carsVotesList)
                if (infoCar[0].equals(carVote.getCar().getBrand())) {
                    if (infoCar[1].equals(carVote.getCar().getModel())) {
                        if (infoCar.length == 3) {
                            if (infoCar[2].equals(carVote.getCar().getSubModel())) {
                                carVote.countVotesIncr();
                                isCar = true;
                                log.info("Добавлен голос за автомобиль: {}", carText);
                            }
                        } else {
                            carVote.countVotesIncr();
                            isCar = true;
                            log.info("Добавлен голос за автомобиль: {}", carText);
                        }
                    }
                }
        } else {
            log.info("Передано слишком длинное название автомобиля: {}", carText);
        }

        return isCar;
    }

    public int addCarsCount() {
        System.out.println(START_VOTE);
        try {
            int count = new Scanner(System.in).nextInt();
            return count;
        } catch (InputMismatchException e) {
            log.error("Передано необрабатываемое значение count, exception: {}", e.getMessage());
            return -1;
        }
    }

    public void addCars(int count) {
        for (int i = 1; i <= count; i++) {
            System.out.println(messageService.getCarNumberRequest(i));
            String carText = new Scanner(System.in).nextLine();
            boolean isFullCarNameCorrect = isFullCarNameCorrect(carText);

            while (!isFullCarNameCorrect) {
                log.error("Передано некорректное название автомобиля №{}: {} ", i, carText);
                System.out.println(ERROR_CAR_NAME);
                carText = new Scanner(System.in).nextLine();
                isFullCarNameCorrect = isFullCarNameCorrect(carText);
            }
        }
        System.out.println(VOTE_CREATED);
    }

    public void addCar(Car car) {
        CarVote carVote = new CarVote(car);
        carsVotesList.add(carVote);
    }

    public boolean isFullCarNameCorrect(String unitedText) {
        boolean isCar = false;
        String[] infoCar = arrayInfoCar(unitedText);
        if (infoCar.length > 3) {
            return isCar;
        }

        try {
            if (!isCarName(infoCar)) {
                return isCar;
            }
            createNewCar(infoCar);
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Передано некорректное название автомобиля, exception: {}, для запроса: {}",
                    e.getMessage(), unitedText);
            System.out.println(ERROR_CAR_NAME);
            String carText = new Scanner(System.in).nextLine();
            boolean isFullCarNameCorrect = isFullCarNameCorrect(carText);

            while (!isFullCarNameCorrect) {
                log.error("Передано некорректное название автомобиля: {} ", carText);
                System.out.println(ERROR_CAR_NAME);
                carText = new Scanner(System.in).nextLine();
                isFullCarNameCorrect = isFullCarNameCorrect(carText);
            }
        }

        isCar = true;
        return isCar;
    }

    public void createNewCar(String[] infoCar) {
        String brand = infoCar[0];
        String model = infoCar[1];
        String subModel = "";

        if (infoCar.length == 3) {
            subModel = infoCar[2];
        }

        Car car = new Car(brand, model, subModel);
        addCar(car);
    }

    public boolean isCarName(String[] infoCar) throws ArrayIndexOutOfBoundsException {
        boolean isCarName = false;

        String regexBrand = "[а-яА-Яa-zA-Z]";
        String brand = infoCar[0];
        final Pattern pattern = Pattern.compile(regexBrand);
        final Matcher matcher = pattern.matcher(brand);
        boolean isBrand = matcher.find();

        String regexModel = "[а-яА-Яa-zA-Z]*[0-9]*[а-яА-Яa-zA-Z]*[0-9]*[а-яА-Яa-zA-Z]*";
        String model = infoCar[1];
        final Pattern patternModel = Pattern.compile(regexModel);
        final Matcher matcherModel = patternModel.matcher(model);
        boolean isModel = matcherModel.find();

        if (isBrand && isModel) {
            isCarName = true;
        }

        if (infoCar.length == 3) {
            String subModel = infoCar[2];
            final Matcher matcherSubModel = patternModel.matcher(subModel);
            boolean isSubModel = matcherSubModel.find();

            if (!isCarName && isSubModel) {
                isCarName = false;
            }
        }

        return isCarName;
    }

    public String[] arrayInfoCar(String unitedText) {
        return unitedText.trim()
                .split("\\s+");
    }

}