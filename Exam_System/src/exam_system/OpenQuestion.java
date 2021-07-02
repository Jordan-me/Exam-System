package exam_system;

public class OpenQuestion extends Question {
	private Answer solution;

	public OpenQuestion(String theQuestion, Answer solution) {
		super(theQuestion);
		this.solution = solution;
	}

	public OpenQuestion(String tQues, String sQues) {
		super(tQues);
		Answer sol = new Answer(sQues);
		this.solution = sol;
	}

	public Answer getSolution() {
		return solution;
	}

	public boolean equals(Object o) {
		if (super.equals(o)) {
			OpenQuestion q = (OpenQuestion) o;
			if (q.solution.equals(this.solution)) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append(solution.toString());
		return sb.toString();
	}
	public boolean equals(String txt) {
		System.out.println("TEXT:  " + txt + "         Curren:  " + this.getTheQuestion());
		if ((this.getTheQuestion()).contentEquals(txt)) {
			return true;
		}
		System.out.println("false");
		return false;
	}


}
