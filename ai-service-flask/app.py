print("🔥 app.py is executing")


from flask import Flask, request, jsonify
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

app = Flask(__name__)

def evaluate_answer(user_answer, model_answer):
    corpus = [user_answer, model_answer]

    vectorizer = TfidfVectorizer()
    tfidf_matrix = vectorizer.fit_transform(corpus)

    similarity = cosine_similarity(
        tfidf_matrix[0:1],
        tfidf_matrix[1:2]
    )[0][0]

    score = int(similarity * 100)

    if similarity >= 0.75:
        feedback = "Excellent answer. Covers most key points."
    elif similarity >= 0.5:
        feedback = "Good answer but missing some important concepts."
    elif similarity >= 0.3:
        feedback = "Average answer. Needs more clarity and depth."
    else:
        feedback = "Poor answer. Revise core concepts."

    return similarity, score, feedback


@app.route("/evaluate", methods=["POST"])
def evaluate():
    data = request.get_json()

    user_answer = data.get("user_answer", "")
    model_answer = data.get("model_answer", "")

    similarity, score, feedback = evaluate_answer(user_answer, model_answer)

    return jsonify({
        "similarity": round(similarity, 2),
        "score": score,
        "feedback": feedback
    })


if __name__ == "__main__":
    print("🚀 Starting Flask AI Evaluation Service...")
    app.run(host="127.0.0.1", port=5000, debug=True)
