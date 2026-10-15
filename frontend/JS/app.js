"use strict";

const API_BASE_URL = "http://localhost:8080";

/* DOM elements */
const languageSelect = document.getElementById("language");
const codeEditor = document.getElementById("code");
const submitButton = document.getElementById("submit-btn");
const resetButton = document.getElementById("reset-btn");

const problemLoading = document.getElementById("problemLoading");
const problemError = document.getElementById("problemError");
const problemContent = document.getElementById("problemContent");

const problemIdLabel = document.getElementById("problemIdLabel");
const problemMetaId = document.getElementById("problemMetaId");
const problemTitle = document.getElementById("problemTitle");
const problemDifficulty = document.getElementById("problemDifficulty");
const problemDescription = document.getElementById("problemDescription");
const problemInput = document.getElementById("problemInput");
const problemOutput = document.getElementById("problemOutput");
const problemConstraints = document.getElementById("problemConstraints");

const editorStatus = document.getElementById("editorStatus");

const resultStatus = document.getElementById("resultStatus");
const resultPlaceholder = document.getElementById("resultPlaceholder");
const resultDetails = document.getElementById("resultDetails");
const verdictText = document.getElementById("verdictText");
const passedTestsElement = document.getElementById("passedTests");
const totalTestsElement = document.getElementById("totalTests");
const executionTimeElement = document.getElementById("executionTime");
const memoryUsedElement = document.getElementById("memoryUsed");
const resultMessage = document.getElementById("resultMessage");
const submissionIdElement = document.getElementById("submissionId");
const loginLink = document.getElementById("loginLink");

/* State */
const params = new URLSearchParams(window.location.search);
const problemIdFromUrl =
    params.get("problem_id") || params.get("problemId");

let currentProblem = null;
let isSubmitting = false;

const DEFAULT_CODE = `#include <iostream>
using namespace std;

int main() {
    // Write your solution here

    return 0;
}`;


/* ==========================================
   1. API RESPONSE HELPERS
========================================== */

async function readResponse(response) {
    const text = await response.text();

    if (!text) {
        return {};
    }

    try {
        return JSON.parse(text);
    } catch {
        return { message: text };
    }
}

function getErrorMessage(data, status) {
    if (data && typeof data === "object") {
        if (data.error) return String(data.error);
        if (data.message) return String(data.message);

        const messages = Object.values(data)
            .filter(value => typeof value === "string");

        if (messages.length > 0) {
            return messages.join(", ");
        }
    }

    return `Request failed with HTTP ${status}.`;
}

function normalizeDifficulty(value) {
    const difficulty = String(value || "Unknown")
        .trim()
        .toLowerCase();

    return difficulty.charAt(0).toUpperCase() + difficulty.slice(1);
}

function setText(element, value, fallback = "Not provided") {
    if (element) {
        element.textContent =
            value !== null && value !== undefined && String(value).trim() !== ""
                ? String(value)
                : fallback;
    }
}

function formatMetric(value, suffix = "") {
    if (value === null || value === undefined || value === "") {
        return "Not available";
    }

    return `${value}${suffix}`;
}


/* ==========================================
   2. RESULT DISPLAY
========================================== */

function resetResultDisplay(message = "Your submission details will appear here.") {
    resultPlaceholder.hidden = false;
    resultPlaceholder.textContent = message;

    resultDetails.hidden = true;

    verdictText.textContent = "";
    verdictText.className = "verdict";

    passedTestsElement.textContent = "—";
    totalTestsElement.textContent = "—";
    executionTimeElement.textContent = "—";
    memoryUsedElement.textContent = "—";

    resultMessage.textContent = "";
    resultMessage.hidden = true;
    resultMessage.className = "result-message";

    submissionIdElement.textContent = "";

    resultStatus.textContent = "Not submitted";
}

function showNotice(title, message, isError = false) {
    resultPlaceholder.hidden = false;
    resultPlaceholder.textContent = `${title}: ${message}`;

    resultDetails.hidden = true;

    resultStatus.textContent = isError ? "Error" : "Information";
}

function displaySubmissionResult(data) {
    resultPlaceholder.hidden = true;
    resultDetails.hidden = false;

    const status = String(data.status || "").toUpperCase();
    const verdict = String(data.verdict || "").toUpperCase();

    const finalLabel = verdict || status || "SUBMITTED";

    verdictText.textContent = finalLabel;
    verdictText.className = "verdict";

    if (verdict === "ACCEPTED") {
        verdictText.classList.add("accepted");
    } else if (
        verdict === "WRONG_ANSWER" ||
        verdict === "COMPILATION_ERROR" ||
        verdict === "RUNTIME_ERROR" ||
        verdict === "TIME_LIMIT_EXCEEDED" ||
        status === "FAILED"
    ) {
        verdictText.classList.add("failed");
    } else {
        verdictText.classList.add("pending");
    }

    passedTestsElement.textContent =
        formatMetric(data.passedTests);

    totalTestsElement.textContent =
        formatMetric(data.totalTests);

    executionTimeElement.textContent =
        formatMetric(data.executionTime, " ms");

    memoryUsedElement.textContent =
        data.memoryUsed === null || data.memoryUsed === undefined
            ? "Not available"
            : `${data.memoryUsed} KB`;

    resultStatus.textContent =
        status || (verdict ? "Completed" : "Submitted");

    /*
     * SubmissionResponse does not necessarily contain the P3 error
     * message. Display only messages actually returned by P2.
     */
    const message = data.message || data.error || "";

    if (message) {
        resultMessage.textContent = String(message);
        resultMessage.hidden = false;
    } else {
        resultMessage.hidden = true;
        resultMessage.textContent = "";
    }

    if (data.submissionId !== null && data.submissionId !== undefined) {
        submissionIdElement.textContent =
            `Submission ID: ${data.submissionId}`;
    } else {
        submissionIdElement.textContent = "";
    }

    editorStatus.textContent = "Submission processed";
}


/* ==========================================
   3. LOAD PROBLEM FROM P2
========================================== */

async function loadProblem() {
    if (!problemIdFromUrl || !/^\d+$/.test(problemIdFromUrl)) {
        problemLoading.hidden = true;
        problemContent.hidden = true;
        problemError.hidden = false;
        problemError.textContent =
            "No valid problem ID was provided. Return to the Problems page and select a problem.";

        problemIdLabel.textContent = "No problem selected";
        submitButton.disabled = true;
        editorStatus.textContent = "Select a problem first";
        return;
    }

    problemLoading.hidden = false;
    problemLoading.textContent = "Loading problem details...";
    problemError.hidden = true;
    problemContent.hidden = true;

    problemIdLabel.textContent = `Problem #${problemIdFromUrl}`;
    submitButton.disabled = true;

    try {
        const response = await fetch(
            `${API_BASE_URL}/api/problems/${encodeURIComponent(problemIdFromUrl)}`,
            {
                method: "GET",
                credentials: "include",
                headers: {
                    Accept: "application/json"
                }
            }
        );

        const data = await readResponse(response);

        if (!response.ok) {
            throw new Error(getErrorMessage(data, response.status));
        }

        if (!data || data.problemId === undefined) {
            throw new Error("The backend returned an unexpected problem response.");
        }

        currentProblem = data;
        renderProblem(data);

        problemLoading.hidden = true;
        problemError.hidden = true;
        problemContent.hidden = false;

        resetResultDisplay(
            "Problem loaded. Write your C++ solution and submit it."
        );

        editorStatus.textContent = "Ready to submit";
        submitButton.disabled = false;

    } catch (error) {
        console.error("Problem loading failed:", error);

        problemLoading.hidden = true;
        problemContent.hidden = true;
        problemError.hidden = false;

        problemError.textContent =
            error.message === "Failed to fetch"
                ? "Cannot connect to P2. Confirm that Spring Boot is running and CORS allows your Live Server origin."
                : error.message;

        problemIdLabel.textContent = "Problem unavailable";
        editorStatus.textContent = "Unable to load problem";
        submitButton.disabled = true;
    }
}


/* ==========================================
   4. RENDER PROBLEM DETAILS
========================================== */

function renderProblem(problem) {
    setText(problemTitle, problem.problemName, "Untitled Problem");
    setText(problemMetaId, `Problem ID: ${problem.problemId}`);
    setText(problemDescription, problem.description, "No description available.");
    setText(problemInput, problem.inputDescription, "Input format not provided.");
    setText(problemOutput, problem.outputDescription, "Output format not provided.");
    setText(problemConstraints, problem.constraints, "No constraints provided.");

    const difficulty = normalizeDifficulty(problem.difficulty);

    problemDifficulty.textContent = difficulty;
    problemDifficulty.className = "difficulty";

    if (difficulty === "Medium") {
        problemDifficulty.classList.add("medium");
    } else if (difficulty === "Hard") {
        problemDifficulty.classList.add("hard");
    }

    problemIdLabel.textContent = `Problem #${problem.problemId}`;

    document.title =
        `${problem.problemName || "Problem"} | CodeForge`;
}


/* ==========================================
   5. SUBMIT CODE TO P2
========================================== */

async function submitCode() {
    if (isSubmitting) return;

    if (!currentProblem) {
        showNotice(
            "Problem unavailable",
            "Wait for the problem details to load before submitting.",
            true
        );
        return;
    }

    const storedUserId = sessionStorage.getItem("userId");

    if (!storedUserId || !/^\d+$/.test(storedUserId)) {
        showNotice(
            "Login required",
            "Log in before submitting your solution.",
            true
        );
        return;
    }

    const sourceCode = codeEditor.value.trim();

    if (!sourceCode) {
        showNotice("Code required", "Write your solution before submitting.", true);
        codeEditor.focus();
        return;
    }

    const language = languageSelect.value;

    if (language !== "CPP") {
        showNotice(
            "Unsupported language",
            "The current backend execution service supports C++ only.",
            true
        );
        return;
    }

    const payload = {
        userId: Number(storedUserId),
        problemId: Number(currentProblem.problemId),
        language,
        sourceCode
    };

    isSubmitting = true;
    submitButton.disabled = true;
    submitButton.textContent = "Submitting...";
    resetButton.disabled = true;

    editorStatus.textContent = "Sending submission to P2...";
    resetResultDisplay("P2 is processing your code. Please wait.");

    try {
        const response = await fetch(`${API_BASE_URL}/api/submissions`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            body: JSON.stringify(payload)
        });

        const data = await readResponse(response);

        if (!response.ok) {
            throw new Error(getErrorMessage(data, response.status));
        }

        displaySubmissionResult(data);

    } catch (error) {
        console.error("Submission failed:", error);

        showNotice(
            "Submission request failed",
            error.message === "Failed to fetch"
                ? "Cannot reach P2. Check that Spring Boot is running and CORS/session settings are correct."
                : error.message,
            true
        );

        editorStatus.textContent = "Submission failed";
    } finally {
        isSubmitting = false;
        submitButton.disabled = !currentProblem;
        submitButton.textContent = "Submit Code";
        resetButton.disabled = false;
    }
}


/* ==========================================
   6. RESET CODE
========================================== */

function resetCode() {
    if (isSubmitting) return;

    const confirmed = window.confirm(
        "Reset the editor to the default C++ template?"
    );

    if (!confirmed) return;

    codeEditor.value = DEFAULT_CODE;
    codeEditor.focus();

    editorStatus.textContent = "Code reset";
}


/* ==========================================
   7. TAB KEY SUPPORT
========================================== */

codeEditor.addEventListener("keydown", function (event) {
    if (event.key !== "Tab") return;

    event.preventDefault();

    const start = codeEditor.selectionStart;
    const end = codeEditor.selectionEnd;

    codeEditor.value =
        codeEditor.value.substring(0, start) +
        "    " +
        codeEditor.value.substring(end);

    codeEditor.selectionStart = codeEditor.selectionEnd = start + 4;
});


/* ==========================================
   8. AUTH NAVIGATION
========================================== */

if (loginLink && sessionStorage.getItem("userId")) {
    loginLink.textContent = "Account";
    loginLink.href = "dashboard.html";
}


/* ==========================================
   9. EVENT LISTENERS
========================================== */

submitButton.addEventListener("click", submitCode);
resetButton.addEventListener("click", resetCode);


/* ==========================================
   10. INITIALIZE
========================================== */

submitButton.disabled = true;

resetResultDisplay();

loadProblem();
