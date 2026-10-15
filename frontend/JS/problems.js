const API_URL = "http://localhost:8080/api/problems";

// DOM elements
const problemList = document.getElementById("problemList");
const searchInput = document.getElementById("problemSearch");
const filterButtons = document.querySelectorAll(".filter-btn");
const visibleCount = document.getElementById("visibleCount");
const emptyState = document.getElementById("emptyState");
const resetButton = document.getElementById("resetFilters");
const pageNote = document.querySelector(".page-note");

// Application state
let problems = [];
let selectedDifficulty = "all";
let isLoading = true;


/* ==========================================
   1. LOAD PROBLEMS FROM P2
========================================== */

async function loadProblems() {
    isLoading = true;
    showMessage("Loading problems from CodeForge backend...");

    try {
        const response = await fetch(API_URL, {
            method: "GET",
            credentials: "include",
            headers: {
                "Accept": "application/json"
            }
        });

        if (!response.ok) {
            throw new Error(
                `Request failed with HTTP ${response.status}.`
            );
        }

        const data = await response.json();

        if (!Array.isArray(data)) {
            throw new Error(
                "Unexpected API response. Expected a JSON array."
            );
        }

        problems = data.map(normalizeProblem);

        if (problems.some(problem => problem.problemId == null)) {
            throw new Error(
                "The API returned a problem without a problemId."
            );
        }

        isLoading = false;

        renderStatistics();
        renderProblems();

        if (pageNote) {
            pageNote.textContent =
                `Loaded ${problems.length} problems from the backend.`;
        }

    } catch (error) {
        isLoading = false;
        console.error("Could not load problems:", error);

        problems = [];

        renderStatistics();
        renderProblems();

        showMessage(
            error.message === "Failed to fetch"
                ? "Cannot connect to P2. Check that Spring Boot is running and CORS allows http://127.0.0.1:5500."
                : error.message
        );

        if (pageNote) {
            pageNote.textContent =
                "Problem data could not be loaded from the backend.";
        }
    }
}


/* ==========================================
   2. NORMALIZE P2 DTO
========================================== */

function normalizeProblem(problem) {
    return {
        problemId: problem.problemId,
        problemName: problem.problemName ?? "Untitled Problem",
        description: problem.description ?? "",
        difficulty: normalizeDifficulty(problem.difficulty),
        inputDescription:
            problem.inputDescription ?? "Not provided.",
        outputDescription:
            problem.outputDescription ?? "Not provided.",
        constraints:
            problem.constraints ?? "Not provided."
    };
}

function normalizeDifficulty(value) {
    const difficulty = String(value ?? "").trim().toLowerCase();

    if (difficulty === "easy") return "Easy";
    if (difficulty === "medium") return "Medium";
    if (difficulty === "hard") return "Hard";

    return "Unknown";
}


/* ==========================================
   3. DISPLAY LOADING AND ERROR MESSAGES
========================================== */

function showMessage(message) {
    problemList.replaceChildren();

    const messageElement = document.createElement("div");
    messageElement.className = "loading-cell";
    messageElement.setAttribute("role", "status");
    messageElement.textContent = message;

    problemList.appendChild(messageElement);

    visibleCount.textContent = "0";
    emptyState.hidden = true;
}


/* ==========================================
   4. UPDATE STATISTICS
========================================== */

function renderStatistics() {
    document.getElementById("totalCount").textContent =
        problems.length;

    document.getElementById("easyCount").textContent =
        problems.filter(p => p.difficulty === "Easy").length;

    document.getElementById("mediumCount").textContent =
        problems.filter(p => p.difficulty === "Medium").length;

    document.getElementById("hardCount").textContent =
        problems.filter(p => p.difficulty === "Hard").length;
}


/* ==========================================
   5. SEARCH AND FILTER
========================================== */

function renderProblems() {
    if (isLoading) return;

    const searchTerm = searchInput.value.trim().toLowerCase();

    const filteredProblems = problems.filter(problem => {
        const searchableText =
            `${problem.problemName} ${problem.description}`.toLowerCase();

        const matchesSearch = searchableText.includes(searchTerm);

        const matchesDifficulty =
            selectedDifficulty === "all" ||
            problem.difficulty.toLowerCase() === selectedDifficulty;

        return matchesSearch && matchesDifficulty;
    });

    problemList.replaceChildren();

    visibleCount.textContent = filteredProblems.length;
    emptyState.hidden = filteredProblems.length !== 0;

    if (filteredProblems.length === 0) {
        if (problems.length === 0) {
            showMessage("No problems are currently available.");
        }
        return;
    }

    filteredProblems.forEach((problem, index) => {
        problemList.appendChild(createProblemRow(problem, index));
    });
}


/* ==========================================
   6. CREATE LEETCODE-STYLE PROBLEM ROW
========================================== */

function createProblemRow(problem, index) {
    const row = document.createElement("div");
    row.className = "problem-row";

    // Problem information container
    const main = document.createElement("div");
    main.className = "problem-main";

    // Visible row number
    const number = document.createElement("span");
    number.className = "problem-number";
    number.textContent = `${index + 1}.`;

    // Name and description
    const info = document.createElement("div");
    info.className = "problem-info";

    const name = document.createElement("div");
    name.className = "problem-name";
    name.textContent = problem.problemName;

    const description = document.createElement("p");
    description.className = "problem-description";
    description.textContent = problem.description;

    info.append(name, description);
    main.append(number, info);

    // Difficulty badge
    const difficulty = document.createElement("span");
    difficulty.className = "difficulty-badge";

    const difficultyClass = problem.difficulty.toLowerCase();

    if (["easy", "medium", "hard"].includes(difficultyClass)) {
        difficulty.classList.add(difficultyClass);
    }

    difficulty.textContent = problem.difficulty;

    // Solve button
    const solveButton = document.createElement("button");
    solveButton.type = "button";
    solveButton.className = "view-btn";
    solveButton.textContent = "Solve";

    solveButton.addEventListener("click", () => {
        const editorUrl =
            `editor.html?problem_id=${encodeURIComponent(problem.problemId)}`;

        window.location.href = editorUrl;
    });

    row.append(main, difficulty, solveButton);

    return row;
}


/* ==========================================
   7. DIFFICULTY FILTER BUTTONS
========================================== */

filterButtons.forEach(button => {
    button.addEventListener("click", () => {
        selectedDifficulty = button.dataset.filter;

        filterButtons.forEach(item => {
            const isActive = item === button;

            item.classList.toggle("active", isActive);
            item.setAttribute("aria-pressed", String(isActive));
        });

        renderProblems();
    });
});


/* ==========================================
   8. RESET SEARCH AND FILTERS
========================================== */

function resetFilters() {
    searchInput.value = "";
    selectedDifficulty = "all";

    filterButtons.forEach(button => {
        const isActive = button.dataset.filter === "all";

        button.classList.toggle("active", isActive);
        button.setAttribute("aria-pressed", String(isActive));
    });

    renderProblems();
}

searchInput.addEventListener("input", renderProblems);
resetButton.addEventListener("click", resetFilters);


/* ==========================================
   9. INITIALIZE PAGE
========================================== */

loadProblems();