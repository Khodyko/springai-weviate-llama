# Проект-тестирование работы Spring AI

Это проект для тестирования работы Spring AI.

---

## Требования для запуска

### Ollama

Для работы необходимо запустить Ollama.  
Инструкции по скачиванию и запуску:

1. Скачать Ollama для linux:  
   [https://ollama.com/download](https://ollama.com/download)

2. Запустить модель llama3:8b:  
   В терминале выполните команду:
   ```bash
   ollama run llama3:8b

# Использование Weaviate

Для использования Weaviate его нужно запустить через Docker.

## Инструкция по запуску

1. Следуйте официальной документации по установке через Docker:  
   [https://docs.weaviate.io/deploy/installation-guides/docker-installation](https://docs.weaviate.io/deploy/installation-guides/docker-installation)

2. Запустите контейнер Weaviate командой:

```bash
sudo docker run -p 8080:8080 -p 50051:50051 cr.weaviate.io/semitechnologies/weaviate:1.31.5