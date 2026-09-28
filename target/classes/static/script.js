document.addEventListener("DOMContentLoaded", function () {

    initializeUsers();

    setupLogin();

    setupSignup();

    updateDate();

    checkLogin();

    updatePercentage();

});


/* =========================================
   USERS
========================================= */

function initializeUsers() {

    let users = JSON.parse(
        localStorage.getItem("examcraftUsers")
    );

    if (!users) {

        users = [];

        localStorage.setItem(
            "examcraftUsers",
            JSON.stringify(users)
        );
    }
}


/* =========================================
   LOGIN
========================================= */

function setupLogin() {

    const loginForm =
        document.getElementById("loginForm");

    loginForm.addEventListener("submit", function (event) {

        event.preventDefault();

        const username =
            document.getElementById("username").value.trim();

        const password =
            document.getElementById("password").value;

        const error =
            document.getElementById("loginError");

        const users =
            JSON.parse(
                localStorage.getItem("examcraftUsers")
            ) || [];

        const user =
            users.find(
                u =>
                    u.username === username &&
                    u.password === password
            );

        if (!user) {

            error.textContent =
                "Invalid username or password.";

            return;
        }

        localStorage.setItem(
            "examcraftLoggedIn",
            "true"
        );

        localStorage.setItem(
            "examcraftUser",
            username
        );

        error.textContent = "";

        openApplication(username);

    });
}


/* =========================================
   SIGN UP
========================================= */

function setupSignup() {

    const signupForm =
        document.getElementById("signupForm");

    signupForm.addEventListener("submit", function (event) {

        event.preventDefault();

        const username =
            document
                .getElementById("signupUsername")
                .value
                .trim();

        const password =
            document
                .getElementById("signupPassword")
                .value;

        const confirmPassword =
            document
                .getElementById("signupConfirmPassword")
                .value;

        const message =
            document.getElementById("signupMessage");

        if (username.length < 3) {

            message.style.color = "#dc2626";

            message.textContent =
                "Username must contain at least 3 characters.";

            return;
        }

        if (password.length < 6) {

            message.style.color = "#dc2626";

            message.textContent =
                "Password must contain at least 6 characters.";

            return;
        }

        if (password !== confirmPassword) {

            message.style.color = "#dc2626";

            message.textContent =
                "Passwords do not match.";

            return;
        }

        let users =
            JSON.parse(
                localStorage.getItem("examcraftUsers")
            ) || [];


        const exists =
            users.some(
                user =>
                    user.username.toLowerCase() ===
                    username.toLowerCase()
            );


        if (exists) {

            message.style.color = "#dc2626";

            message.textContent =
                "Username already exists.";

            return;
        }


        users.push({

            username: username,

            password: password,

            createdAt:
                new Date().toISOString()

        });


        localStorage.setItem(
            "examcraftUsers",
            JSON.stringify(users)
        );


        message.style.color = "#059669";

        message.textContent =
            "Account created successfully!";


        document.getElementById("signupForm").reset();


        setTimeout(function () {

            backToLogin();

            document.getElementById("username").value =
                username;

        }, 1000);

    });
}


/* =========================================
   SHOW SIGNUP
========================================= */

function showSignup() {

    document
        .getElementById("loginPage")
        .classList.add("hidden");

    document
        .getElementById("signupPage")
        .classList.remove("hidden");

    document
        .getElementById("signupMessage")
        .textContent = "";
}


/* =========================================
   BACK TO LOGIN
========================================= */

function backToLogin() {

    document
        .getElementById("signupPage")
        .classList.add("hidden");

    document
        .getElementById("loginPage")
        .classList.remove("hidden");

    document
        .getElementById("signupMessage")
        .textContent = "";
}


/* =========================================
   CHECK LOGIN
========================================= */

function checkLogin() {

    const loggedIn =
        localStorage.getItem("examcraftLoggedIn");

    const username =
        localStorage.getItem("examcraftUser");

    if (loggedIn === "true" && username) {

        openApplication(username);

    } else {

        document
            .getElementById("loginPage")
            .classList.remove("hidden");

        document
            .getElementById("mainApp")
            .classList.add("hidden");
    }
}


/* =========================================
   OPEN APPLICATION
========================================= */

function openApplication(username) {

    document
        .getElementById("loginPage")
        .classList.add("hidden");

    document
        .getElementById("signupPage")
        .classList.add("hidden");

    document
        .getElementById("mainApp")
        .classList.remove("hidden");


    document
        .getElementById("loggedUser")
        .textContent = username;

    document
        .getElementById("headerUsername")
        .textContent = username;


    renderQuestions();
    renderUnits();
    renderAttempts();
    updateStats();

}


/* =========================================
   LOGOUT
========================================= */

function logout() {

    localStorage.removeItem(
        "examcraftLoggedIn"
    );

    localStorage.removeItem(
        "examcraftUser"
    );

    location.reload();
}


/* =========================================
   PAGE NAVIGATION
========================================= */

function showPage(pageName, button = null) {

    document
        .querySelectorAll(".page")
        .forEach(page => {

            page.classList.remove("active");

        });


    const target =
        document.getElementById(
            "page-" + pageName
        );


    if (target) {

        target.classList.add("active");

    }


    document
        .querySelectorAll(".nav-btn")
        .forEach(btn => {

            btn.classList.remove("active");

        });


    if (button) {

        button.classList.add("active");

    } else {

        const navButton =
            document.querySelector(
                `.nav-btn[onclick*="'${pageName}'"]`
            );

        if (navButton) {

            navButton.classList.add("active");

        }
    }


    updatePageTitle(pageName);

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });

}


/* =========================================
   PAGE TITLES
========================================= */

function updatePageTitle(page) {

    const titles = {

        dashboard: [
            "Dashboard",
            "Overview of your examination workspace"
        ],

        questions: [
            "Question Bank",
            "Create, organize and manage examination questions."
        ],

        units: [
            "Units",
            "Organize your examination syllabus."
        ],

        generate: [
            "Generate Paper",
            "Create a randomized test paper."
        ],

        attempts: [
            "Attempts & Results",
            "Track examination records and results."
        ]

    };


    if (!titles[page]) return;


    document
        .getElementById("pageTitle")
        .textContent = titles[page][0];

    document
        .getElementById("pageHeading")
        .textContent = titles[page][0];

    document
        .getElementById("pageSubtitle")
        .textContent = titles[page][1];

}


/* =========================================
   SAMPLE QUESTIONS
========================================= */

let questions =
    JSON.parse(
        localStorage.getItem("examcraftQuestions")
    ) || [

        {
            id: 1,
            question: "What is Artificial Intelligence?",
            optionA: "Simulation of human intelligence",
            optionB: "A programming language",
            optionC: "A database",
            optionD: "An operating system",
            correct: "A",
            difficulty: "Easy",
            topic: "AI",
            unit: "Unit 1"
        },

        {
            id: 2,
            question: "Which algorithm uses heuristic search?",
            optionA: "Bubble Sort",
            optionB: "A*",
            optionC: "Linear Search",
            optionD: "Selection Sort",
            correct: "B",
            difficulty: "Medium",
            topic: "Search",
            unit: "Unit 2"
        },

        {
            id: 3,
            question: "What does NLP stand for?",
            optionA: "Network Language Protocol",
            optionB: "Natural Language Processing",
            optionC: "New Learning Program",
            optionD: "Neural Logic Process",
            correct: "B",
            difficulty: "Easy",
            topic: "NLP",
            unit: "Unit 3"
        },

        {
            id: 4,
            question: "Which technique is used in deep learning?",
            optionA: "Neural Networks",
            optionB: "Binary Search",
            optionC: "Hashing",
            optionD: "Sorting",
            correct: "A",
            difficulty: "Medium",
            topic: "Deep Learning",
            unit: "Unit 4"
        },

        {
            id: 5,
            question: "Explain the working principle of a neural network.",
            optionA: "Input processing through layers",
            optionB: "Only database storage",
            optionC: "File compression",
            optionD: "Network routing",
            correct: "A",
            difficulty: "Hard",
            topic: "Neural Networks",
            unit: "Unit 5"
        }

    ];


/* =========================================
   SAVE QUESTIONS
========================================= */

function saveQuestions() {

    localStorage.setItem(
        "examcraftQuestions",
        JSON.stringify(questions)
    );

}


/* =========================================
   RENDER QUESTIONS
========================================= */

function renderQuestions(list = questions) {

    const table =
        document.getElementById("questionTable");

    if (!table) return;

    table.innerHTML = "";


    if (list.length === 0) {

        table.innerHTML = `
            <tr>
                <td colspan="7"
                    style="text-align:center;padding:35px;color:#9ca3af;">
                    No questions found.
                </td>
            </tr>
        `;

        return;
    }


    list.forEach((q, index) => {

        const row =
            document.createElement("tr");


        row.innerHTML = `

            <td>${index + 1}</td>

            <td>
                <strong style="color:#111827;">
                    ${escapeHTML(q.question)}
                </strong>
            </td>

            <td>${escapeHTML(q.topic)}</td>

            <td>${escapeHTML(q.unit)}</td>

            <td>
                <span class="difficulty ${q.difficulty.toLowerCase()}">
                    ${q.difficulty}
                </span>
            </td>

            <td>
                <strong>${q.correct}</strong>
            </td>

            <td>
                <button
                    class="delete-btn"
                    onclick="deleteQuestion(${q.id})"
                >
                    Delete
                </button>
            </td>

        `;


        table.appendChild(row);

    });

}


/* =========================================
   DELETE QUESTION
========================================= */

function deleteQuestion(id) {

    questions =
        questions.filter(
            q => q.id !== id
        );

    saveQuestions();

    renderQuestions();

    updateStats();

}


/* =========================================
   QUESTION FORM
========================================= */

function openQuestionForm() {

    document
        .getElementById("questionForm")
        .style.display = "block";

    document
        .getElementById("questionForm")
        .scrollIntoView({
            behavior: "smooth",
            block: "center"
        });

}


function closeQuestionForm() {

    document
        .getElementById("questionForm")
        .style.display = "none";

}


/* =========================================
   ADD QUESTION
========================================= */

function addQuestion() {

    const question =
        document
            .getElementById("questionText")
            .value
            .trim();

    const optionA =
        document
            .getElementById("optionA")
            .value
            .trim();

    const optionB =
        document
            .getElementById("optionB")
            .value
            .trim();

    const optionC =
        document
            .getElementById("optionC")
            .value
            .trim();

    const optionD =
        document
            .getElementById("optionD")
            .value
            .trim();

    const correct =
        document
            .getElementById("correctAnswer")
            .value;

    const difficulty =
        document
            .getElementById("questionDifficulty")
            .value;

    const topic =
        document
            .getElementById("questionTopic")
            .value
            .trim();

    const unit =
        document
            .getElementById("questionUnit")
            .value
            .trim();


    if (
        !question ||
        !optionA ||
        !optionB ||
        !optionC ||
        !optionD ||
        !correct ||
        !topic ||
        !unit
    ) {

        alert("Please fill all fields.");

        return;
    }


    const newQuestion = {

        id: Date.now(),

        question,

        optionA,
        optionB,
        optionC,
        optionD,

        correct,

        difficulty,

        topic,

        unit

    };


    questions.push(newQuestion);

    saveQuestions();

    renderQuestions();

    updateStats();

    document
        .getElementById("questionForm")
        .querySelectorAll("input, textarea, select")
        .forEach(element => {

            element.value = "";

        });


    document.getElementById(
        "questionDifficulty"
    ).value = "Easy";


    closeQuestionForm();


    alert("Question added successfully!");

}


/* =========================================
   FILTER QUESTIONS
========================================= */

function filterQuestions() {

    const search =
        document
            .getElementById("searchQuestion")
            .value
            .toLowerCase();

    const difficulty =
        document
            .getElementById("difficultyFilter")
            .value;


    const filtered =
        questions.filter(q => {

            const matchesSearch =

                q.question
                    .toLowerCase()
                    .includes(search)

                ||

                q.topic
                    .toLowerCase()
                    .includes(search)

                ||

                q.unit
                    .toLowerCase()
                    .includes(search);


            const matchesDifficulty =
                !difficulty ||
                q.difficulty === difficulty;


            return (
                matchesSearch &&
                matchesDifficulty
            );

        });


    renderQuestions(filtered);

}


/* =========================================
   UNITS
========================================= */

/* =========================================
   UNIT DATA
========================================= */

const unitData = {

    1: {
        title: "Unit 1 - Introduction to Artificial Intelligence",

        subtitle: "AI Fundamentals and Intelligent Agents",

        syllabus: [
            {
                title: "Introduction to Artificial Intelligence",
                description: "Meaning, history, applications and importance of Artificial Intelligence."
            },
            {
                title: "Intelligent Systems",
                description: "Characteristics of intelligent systems and how machines perform intelligent tasks."
            },
            {
                title: "Agents and Environments",
                description: "Concept of agents, environments and interaction between an agent and its environment."
            },
            {
                title: "Types of Agents",
                description: "Simple reflex agents, model-based agents, goal-based agents and utility-based agents."
            },
            {
                title: "PEAS Framework",
                description: "Performance measure, Environment, Actuators and Sensors used for describing intelligent agents."
            },
            {
                title: "Applications of AI",
                description: "Healthcare, education, robotics, finance, transportation and recommendation systems."
            }
        ],

        notes: [
            {
                title: "What is AI?",
                text: "Artificial Intelligence is a branch of computer science that focuses on creating systems capable of performing tasks that normally require human intelligence."
            },
            {
                title: "Intelligent Agent",
                text: "An intelligent agent perceives its environment using sensors and performs actions using actuators."
            },
            {
                title: "PEAS",
                text: "PEAS stands for Performance measure, Environment, Actuators and Sensors. It is used to describe the task environment of an intelligent agent."
            }
        ]
    },


    2: {
        title: "Unit 2 - Search Algorithms",

        subtitle: "Problem Solving and State Space Search",

        syllabus: [
            {
                title: "Problem Formulation",
                description: "Initial state, goal state, actions, transition model and path cost."
            },
            {
                title: "State Space Representation",
                description: "Representing a problem as a collection of states and transitions."
            },
            {
                title: "Breadth First Search",
                description: "Uninformed search algorithm that explores nodes level by level."
            },
            {
                title: "Depth First Search",
                description: "Search technique that explores the deepest available node before backtracking."
            },
            {
                title: "Uniform Cost Search",
                description: "Expands the node having the lowest path cost."
            },
            {
                title: "Greedy Best First Search",
                description: "Uses a heuristic function to select the apparently closest node to the goal."
            },
            {
                title: "A* Search",
                description: "Combines actual path cost and heuristic cost to find efficient solutions."
            }
        ],

        notes: [
            {
                title: "Search Problem",
                text: "A search problem consists of an initial state, possible actions, transition model, goal test and path cost."
            },
            {
                title: "A* Algorithm",
                text: "A* evaluates nodes using f(n) = g(n) + h(n), where g(n) is the path cost and h(n) is the estimated cost to reach the goal."
            },
            {
                title: "Heuristic",
                text: "A heuristic is an estimate of the cost required to reach the goal from a particular state."
            }
        ]
    },


    3: {
        title: "Unit 3 - Knowledge Representation",

        subtitle: "Logic, Reasoning and Knowledge Based Systems",

        syllabus: [
            {
                title: "Knowledge Representation",
                description: "Methods used to represent facts, rules and relationships in AI systems."
            },
            {
                title: "Propositional Logic",
                description: "Representing knowledge using propositions and logical connectives."
            },
            {
                title: "Predicate Logic",
                description: "Representation of objects, properties, relations and quantified statements."
            },
            {
                title: "First Order Logic",
                description: "Constants, variables, predicates, functions and quantifiers."
            },
            {
                title: "Inference",
                description: "Deriving new information from existing knowledge."
            },
            {
                title: "Resolution",
                description: "A rule of inference used for proving logical statements."
            },
            {
                title: "DPLL Algorithm",
                description: "A complete algorithm for solving Boolean satisfiability problems."
            }
        ],

        notes: [
            {
                title: "Knowledge Base",
                text: "A knowledge base stores facts and rules that an intelligent system can use for reasoning."
            },
            {
                title: "FOL",
                text: "First Order Logic allows AI systems to represent objects and relationships using predicates and quantifiers."
            },
            {
                title: "Inference",
                text: "Inference is the process of deriving new conclusions from known facts and rules."
            }
        ]
    },


    4: {
        title: "Unit 4 - Machine Learning",

        subtitle: "Learning Methods and Predictive Models",

        syllabus: [
            {
                title: "Introduction to Machine Learning",
                description: "Concept, importance and applications of machine learning."
            },
            {
                title: "Supervised Learning",
                description: "Learning from labelled training data."
            },
            {
                title: "Unsupervised Learning",
                description: "Finding patterns and structures in unlabelled data."
            },
            {
                title: "Classification",
                description: "Predicting categories or classes using machine learning models."
            },
            {
                title: "Regression",
                description: "Predicting continuous numerical values."
            },
            {
                title: "Clustering",
                description: "Grouping similar data points into clusters."
            },
            {
                title: "Model Evaluation",
                description: "Accuracy, precision, recall, F1-score and other evaluation measures."
            }
        ],

        notes: [
            {
                title: "Machine Learning",
                text: "Machine Learning allows computers to learn patterns from data and make predictions or decisions without explicitly programming every rule."
            },
            {
                title: "Supervised Learning",
                text: "Supervised learning uses labelled examples to train a model."
            },
            {
                title: "Unsupervised Learning",
                text: "Unsupervised learning works with unlabelled data to discover hidden patterns."
            }
        ]
    },


    5: {
        title: "Unit 5 - Neural Networks and Deep Learning",

        subtitle: "Neural Models and Modern AI",

        syllabus: [
            {
                title: "Biological Neuron",
                description: "Basic idea of biological neurons and their relationship to artificial neurons."
            },
            {
                title: "Artificial Neuron",
                description: "Inputs, weights, bias, activation function and output."
            },
            {
                title: "Perceptron",
                description: "Basic neural network model used for binary classification."
            },
            {
                title: "Activation Functions",
                description: "Sigmoid, ReLU, Tanh and other activation functions."
            },
            {
                title: "Backpropagation",
                description: "Training technique used to update network weights based on error."
            },
            {
                title: "Deep Neural Networks",
                description: "Networks containing multiple hidden layers."
            },
            {
                title: "Applications of Deep Learning",
                description: "Computer vision, speech recognition, NLP and recommendation systems."
            }
        ],

        notes: [
            {
                title: "Artificial Neuron",
                text: "An artificial neuron receives inputs, applies weights and bias, and passes the result through an activation function."
            },
            {
                title: "ReLU",
                text: "ReLU is a commonly used activation function in deep neural networks and returns zero for negative input values."
            },
            {
                title: "Backpropagation",
                text: "Backpropagation calculates the error contribution of network parameters and helps update weights during training."
            }
        ]
    }

};


/* =========================================
   UNITS
========================================= */

let units = [

    "Unit 1 - Introduction to Artificial Intelligence",
    "Unit 2 - Search Algorithms",
    "Unit 3 - Knowledge Representation",
    "Unit 4 - Machine Learning",
    "Unit 5 - Neural Networks and Deep Learning"

];


function saveUnits() {

    localStorage.setItem(
        "examcraftUnits",
        JSON.stringify(units)
    );

}


function renderUnits() {

    const grid =
        document.getElementById("unitGrid");

    if (!grid) return;

    grid.innerHTML = "";


    units.forEach((unit, index) => {

        const card =
            document.createElement("div");

        card.className = "unit-card";

        card.onclick = function () {
            openUnitDetails(index + 1);
        };


        const data =
            unitData[index + 1];


        card.innerHTML = `

            <div class="unit-number">
                ${String(index + 1).padStart(2, "0")}
            </div>

            <h3>
                ${escapeHTML(
                    data
                        ? data.title
                        : unit
                )}
            </h3>

            <p>
                ${data
                    ? escapeHTML(data.subtitle)
                    : "Syllabus unit"}
            </p>

        `;


        grid.appendChild(card);

    });

}


/* =========================================
   OPEN UNIT
========================================= */

function openUnitDetails(unitNumber) {

    const data =
        unitData[unitNumber];


    if (!data) {

        alert(
            "Detailed syllabus is not available for this unit yet."
        );

        return;

    }


    document
        .getElementById("unitGrid")
        .classList.add("hidden");


    document
        .getElementById("unitDetails")
        .classList.remove("hidden");


    document
        .getElementById("detailUnitNumber")
        .textContent =
        String(unitNumber)
            .padStart(2, "0");


    document
        .getElementById("detailUnitTitle")
        .textContent =
        data.title;


    document
        .getElementById("detailUnitSubtitle")
        .textContent =
        data.subtitle;


    renderUnitSyllabus(data);

    renderUnitNotes(data);

    renderUnitQuestions(unitNumber);

}


/* =========================================
   SYLLABUS
========================================= */

function renderUnitSyllabus(data) {

    const container =
        document.getElementById(
            "unitSyllabus"
        );


    container.innerHTML = "";


    data.syllabus.forEach(
        (topic, index) => {

            container.innerHTML += `

                <div class="syllabus-topic">

                    <div class="topic-number">
                        ${String(index + 1).padStart(2, "0")}
                    </div>

                    <div>

                        <strong>
                            ${escapeHTML(topic.title)}
                        </strong>

                        <p>
                            ${escapeHTML(topic.description)}
                        </p>

                    </div>

                </div>

            `;

        }
    );

}


/* =========================================
   NOTES
========================================= */

function renderUnitNotes(data) {

    const container =
        document.getElementById(
            "unitNotes"
        );


    container.innerHTML = "";


    data.notes.forEach(
        note => {

            container.innerHTML += `

                <div class="note-section">

                    <h4>
                        ${escapeHTML(note.title)}
                    </h4>

                    <p>
                        ${escapeHTML(note.text)}
                    </p>

                </div>

            `;

        }
    );

}


/* =========================================
   UNIT QUESTIONS
========================================= */

function renderUnitQuestions(unitNumber) {

    const container =
        document.getElementById(
            "unitQuestions"
        );


    const unitName =
        "Unit " + unitNumber;


    const related =
        questions.filter(
            q =>
                q.unit
                    .toLowerCase()
                    .includes(
                        unitName.toLowerCase()
                    )
        );


    container.innerHTML = "";


    if (related.length === 0) {

        container.innerHTML = `

            <p style="
                color:#9ca3af;
                font-size:12px;
                padding:15px 0;
            ">
                No questions available for this unit.
            </p>

        `;

        return;

    }


    related.forEach(
        (q, index) => {

            container.innerHTML += `

                <div class="unit-question-item">

                    <div class="unit-question-top">

                        <div class="unit-question-number">
                            ${index + 1}
                        </div>

                        <div class="unit-question-text">
                            ${escapeHTML(q.question)}
                        </div>

                    </div>

                    <div class="unit-question-meta">

                        <span>
                            ${q.difficulty}
                        </span>

                        <span>
                            ${escapeHTML(q.topic)}
                        </span>

                    </div>

                </div>

            `;

        }
    );

}


/* =========================================
   CLOSE UNIT DETAILS
========================================= */

function closeUnitDetails() {

    document
        .getElementById("unitDetails")
        .classList.add("hidden");


    document
        .getElementById("unitGrid")
        .classList.remove("hidden");

}

/* =========================================
   RANDOM QUESTIONS
========================================= */

function getRandomQuestions(
    difficulty,
    count
) {

    const available =
        questions.filter(
            q =>
                q.difficulty ===
                difficulty
        );


    shuffle(available);


    return available.slice(
        0,
        count
    );

}


/* =========================================
   SHUFFLE
========================================= */

function shuffle(array) {

    for (
        let i = array.length - 1;
        i > 0;
        i--
    ) {

        const j =
            Math.floor(
                Math.random() *
                (i + 1)
            );


        [
            array[i],
            array[j]
        ] =
        [
            array[j],
            array[i]
        ];

    }


    return array;

}


/* =========================================
   RENDER PAPER
========================================= */

function renderGeneratedPaper(paper) {

    const container =
        document.getElementById(
            "generatedPaper"
        );


    let html = `

        <div class="generated-paper">

            <span class="eyebrow">
                GENERATED PAPER
            </span>

            <h3>
                ${escapeHTML(paper.title)}
            </h3>

            <p style="
                margin-top:6px;
                color:#9ca3af;
                font-size:10px;
            ">
                Generated on ${paper.date}
            </p>

            <div style="margin-top:20px;">

    `;


    paper.questions.forEach(
        (q, index) => {

            html += `

                <div class="paper-question">

                    <strong>
                        ${index + 1}.
                        ${escapeHTML(q.question)}
                    </strong>

                    <div class="paper-options">

                        <span>
                            A. ${escapeHTML(q.optionA)}
                        </span>

                        <span>
                            B. ${escapeHTML(q.optionB)}
                        </span>

                        <span>
                            C. ${escapeHTML(q.optionC)}
                        </span>

                        <span>
                            D. ${escapeHTML(q.optionD)}
                        </span>

                    </div>

                </div>

            `;

        }
    );


    html += `

            </div>

        </div>

    `;


    container.innerHTML = html;

}


/* =========================================
   PERCENTAGE
========================================= */

function updatePercentage() {

    const easy =
        parseInt(
            document.getElementById(
                "easyPercent"
            )?.value
        ) || 0;


    const medium =
        parseInt(
            document.getElementById(
                "mediumPercent"
            )?.value
        ) || 0;


    const hard =
        parseInt(
            document.getElementById(
                "hardPercent"
            )?.value
        ) || 0;


    const total =
        easy + medium + hard;


    const totalElement =
        document.getElementById(
            "percentageTotal"
        );


    if (totalElement) {

        totalElement.textContent =
            total + "%";


        totalElement.style.color =
            total === 100
                ? "#059669"
                : "#dc2626";

    }


    const easyDisplay =
        document.getElementById(
            "easyDisplay"
        );

    const mediumDisplay =
        document.getElementById(
            "mediumDisplay"
        );

    const hardDisplay =
        document.getElementById(
            "hardDisplay"
        );


    if (easyDisplay)
        easyDisplay.textContent =
            easy + "%";


    if (mediumDisplay)
        mediumDisplay.textContent =
            medium + "%";


    if (hardDisplay)
        hardDisplay.textContent =
            hard + "%";

}


/* =========================================
   ATTEMPTS
========================================= */

function renderAttempts() {

    const table =
        document.getElementById(
            "attemptTable"
        );

    if (!table) return;


    table.innerHTML = "";


    if (generatedPapers.length === 0) {

        table.innerHTML = `

            <tr>

                <td
                    colspan="4"
                    style="
                        text-align:center;
                        padding:35px;
                        color:#9ca3af;
                    "
                >
                    No generated papers yet.
                </td>

            </tr>

        `;

        return;

    }


    [...generatedPapers]
        .reverse()
        .forEach(
            (paper, index) => {

                const row =
                    document.createElement("tr");


                row.innerHTML = `

                    <td>
                        ${index + 1}
                    </td>

                    <td>
                        <strong
                            style="color:#111827;"
                        >
                            ${escapeHTML(
                                paper.title
                            )}
                        </strong>
                    </td>

                    <td>
                        ${paper.date}
                    </td>

                    <td>

                        <span
                            style="
                                color:#635bff;
                                font-weight:700;
                            "
                        >
                            ${paper.questions.length}
                            Questions
                        </span>

                    </td>

                `;


                table.appendChild(row);

            }
        );

}


/* =========================================
   STATISTICS
========================================= */

function updateStats() {

    const questionCount =
        document.getElementById(
            "questionCount"
        );

    const unitCount =
        document.getElementById(
            "unitCount"
        );

    const paperCount =
        document.getElementById(
            "paperCount"
        );

    const attemptCount =
        document.getElementById(
            "attemptCount"
        );


    if (questionCount)
        questionCount.textContent =
            questions.length;


    if (unitCount)
        unitCount.textContent =
            units.length;


    if (paperCount)
        paperCount.textContent =
            generatedPapers.length;


    if (attemptCount)
        attemptCount.textContent =
            generatedPapers.length;


    const heroQuestions =
        document.getElementById(
            "heroQuestions"
        );

    const heroPapers =
        document.getElementById(
            "heroPapers"
        );


    if (heroQuestions)
        heroQuestions.textContent =
            questions.length;


    if (heroPapers)
        heroPapers.textContent =
            generatedPapers.length;

}


/* =========================================
   DATE
========================================= */

function updateDate() {

    const element =
        document.getElementById(
            "currentDate"
        );

    if (!element) return;


    const date =
        new Date();


    element.textContent =
        date.toLocaleDateString(
            "en-IN",
            {
                day: "2-digit",
                month: "short",
                year: "numeric"
            }
        );

}


/* =========================================
   SECURITY / DISPLAY
========================================= */

function escapeHTML(value) {

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");

}