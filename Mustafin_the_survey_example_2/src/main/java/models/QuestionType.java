package models;

public enum QuestionType {
    OPEN_ENDED,
    CLOSED,
    PARTIALLY_OPEN
}

private Question parseQuestion(String line, int surveyId){
    String [] parts = line.split(";");
    String typeStr = parts[0].trim();
    try {
        QuestionType.valueOf(typeStr);

        // Остальной код
    } cath (IllegalArgumentException e) {
        throw new IllegalArgumentException("Unknown question type: " + typeStr);
    }
}