package com.example.pc_builder.model

enum class CategoriaComponente(val rotulo: String, val rotuloCurto: String) {
    PROCESSADOR("Processador", "CPU"),
    PLACA_MAE("Placa-mãe", "Placa-mãe"),
    PLACA_DE_VIDEO("Placa de vídeo", "GPU"),
    MEMORIA_RAM("Memória RAM", "RAM"),
    ARMAZENAMENTO("Armazenamento", "SSD"),
    FONTE("Fonte", "Fonte"),
    GABINETE("Gabinete", "Gabinete"),
    REFRIGERACAO("Refrigeração", "Refrigeração")
}