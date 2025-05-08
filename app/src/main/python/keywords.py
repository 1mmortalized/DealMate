import os
import nltk

script_dir = os.path.dirname(__file__)
nltk_data_path = os.path.join(script_dir, "nltk_data")
nltk.data.path.append(nltk_data_path)

from rake_nltk import Rake

def extract_keywords(text):
    r = Rake()

    # Extract keywords from the text
    r.extract_keywords_from_text(text)

    # Get the ranked keywords
    keywords = r.get_ranked_phrases()
    return keywords