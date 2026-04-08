# Linear Algebra – Assignment 01: 3×3 Matrix Determinant Solver

## Student Information
- **Name:** Tanchingco, John Matthew R.
- **Course:** Math 101 – Linear Algebra, UPHSD Molino Campus
- **Assignment:** Assignment 01 – 3×3 Matrix Determinant Solver

---

## Assigned Matrix

```
| 3  5  4 |
| 6  2  1 |
| 4  3  5 |
```

---

## How to Run

### Java
```bash
javac DeterminantSolver.java
java DeterminantSolver
```

### JavaScript (Node.js)
```bash
node determinant_solver.js
```

---

## Sample Output

```
===================================================
  3x3 MATRIX DETERMINANT SOLVER
  Student: Tanchingco, John Matthew R.
  Assigned Matrix:
===================================================
  |  3   5   4 |
  |  6   2   1 |
  |  4   3   5 |
===================================================

Expanding along Row 1 (cofactor expansion):

  Step 1 - Minor M11: det([2,1],[3,5]) = (2*5) - (1*3) = 10 - 3 = 7
  Step 2 - Minor M12: det([6,1],[4,5]) = (6*5) - (1*4) = 30 - 4 = 26
  Step 3 - Minor M13: det([6,2],[4,3]) = (6*3) - (2*4) = 18 - 8 = 10

  Cofactor C11 = (+1) x 3 x 7 = 21
  Cofactor C12 = (-1) x 5 x 26 = -130
  Cofactor C13 = (+1) x 4 x 10 = 40

  det(M) = 21 + (-130) + 40

===================================================
  DETERMINANT = -69
===================================================
```

---

## Final Determinant Value

**det(M) = -69**

The matrix is **non-singular** (invertible).
