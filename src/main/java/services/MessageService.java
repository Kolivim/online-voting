package services;

import model.CarVote;

import java.util.ArrayList;

public class MessageService {
    public static final String START_VOTE = "Голосование за автомобиль года" +
            "\n\nСколько моделей авто участвуют в голосовании?: ";
    public static final String VOTE_CREATED = "\nГолосование создано! ";
    public static final String END_VOTE = "Голосование завершено!\nЛучший автомобиль года: ";
    public static final String INCORRECT_END_VOTE = "Голосование не состоялось! Не получено ни одного голоса";
    public static final String COUNT_VOTE = "\nКоличество голосов: ";
    public static final String INCORRECT_COUNT_VOTE = "Передано некорректное количество автомобилей. Повторите ввод!\n";
    public static final String CHANGE_REQUEST = "Ваш выбор?: ";
    public static final String VOTE_ADDED = "Ваш голос принят!";
    public static final String VOTE_ERROR = "Ошибка - введите автомобиль из списка!";
    public static final String ENTER_MODEL_FIRST = "Введите модель ";
    public static final String ENTER_MODEL_LAST = "-го автомобиля: ";
    public static final String CHANGE_MODEL = "Выберите модель из списка: ";
    public static final String SCORE_VOTES = "\nДля подсчета голосов введите 0\n";
    public static final String ERROR_CAR_NAME = "Передано некорректное название автомобиля. Повторите ввод!";
    private StringBuilder builder = new StringBuilder();

    public String getStatistics(CarVote maxCarVote) {
        builder.setLength(0);
        builder.append(END_VOTE);
        builder.append(maxCarVote.getCar().getBrand());
        builder.append(" ");
        builder.append(maxCarVote.getCar().getModel());
        builder.append(" ");
        builder.append(maxCarVote.getCar().getSubModel());
        builder.append(COUNT_VOTE);
        builder.append(maxCarVote.getCountVotes());
        return builder.toString();
    }

    public String getCarNumberRequest(int number) {
        builder.setLength(0);
        builder.append(ENTER_MODEL_FIRST);
        builder.append(String.valueOf(number));
        builder.append(ENTER_MODEL_LAST);
        return builder.toString();
    }

    public String infoVoting(ArrayList<CarVote> carsVotesList) {
        builder.setLength(0);
        builder.append(CHANGE_MODEL);

        for (CarVote carVote : carsVotesList) {
            builder.append(carVote.getCar().getBrand());
            builder.append(" ");
            builder.append(carVote.getCar().getModel());
            builder.append(" ");
            builder.append(carVote.getCar().getSubModel());
            builder.append("; ");
        }

        builder.append(SCORE_VOTES);
        return builder.toString();
    }
}