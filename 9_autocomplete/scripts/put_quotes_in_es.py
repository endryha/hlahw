#!/usr/bin/env /usr/local/opt/python@3.9/bin/python3.9
import logging, sys, uuid
from datetime import datetime
from elasticsearch import Elasticsearch
from faker import Faker

logging.basicConfig(stream=sys.stderr, level=logging.WARN)

index_names = ["quotes"]

if __name__ == "__main__":
    total = 10

    if len(sys.argv) == 2:
        total = int(sys.argv[1])

    es = Elasticsearch()
    fake = Faker(["en_US"], use_weighting=False)

    for i in range(total):
        name = fake.first_name() + " " + fake.last_name()
        doc = {
            'author': name,
            'quote': fake.sentence(nb_words=10)
        }

        logging.info('Doc      -> %s', doc)

        for index_name in index_names:
            res = es.index(index=index_name, id=str(uuid.uuid4()), body=doc)

        logging.info('Response -> %s', res)

    for index_name in index_names:
        logging.info('Refresh index %s', index_name)
        try:
            res = es.indices.refresh(index=index_name)
        except (RuntimeError, TypeError, NameError):
            pass