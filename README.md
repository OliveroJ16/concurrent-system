# 🏭 Concurrent Assembly Line Simulation in Java

## 📄 Project Description
This project involves developing a **Java** system that simulates a packaging process on an assembly line using **concurrency APIs**.  
The main goal is to analyze the management of critical sections and the application of **mutual exclusion** mechanisms in scenarios where multiple threads share resources.  

Two approaches are implemented:  
1. **Critical section per conveyor position:** allows multiple threads to concurrently access individual positions.  
2. **Conveyor as a single critical section:** only one thread can access the conveyor at a time.  

The system evaluates the performance and consistency of both versions through execution tests and comparative charts.

---

## 🛠️ Problem Description
- The assembly line has **placers** and **packers**:  
  - 6 placers and 6 packers, distributed in pairs for 3 types of products.  
- Products are generated randomly:  
  - Type 1: 0 - 299  
  - Type 2: 300 - 599  
  - Type 3: 600 - 1000  
- The **conveyor belt** has a maximum capacity of 500 positions.  
  - Placers fill the conveyor until `X` items are reached.  
  - Packers remove products of their type and store them in shared containers.  
- **Critical shared variables:**  
  - Total products placed  
  - Total products packed  
  These variables require **mutual exclusion** to maintain consistency.  

---

## 📊 Results and Execution Analysis

### Asynchronous Version
The asynchronous version allows multiple threads to concurrently access individual conveyor positions, maintaining **stable and efficient performance**.  

- Execution times increase gradually as the number of products rises.  
- For example, with 1,000 products the average was **70.33 ms**, while with 100,000 products it increased to **304.33 ms**.  
- This demonstrates that concurrent access optimizes resource utilization and keeps times relatively low even under high loads.  

**Results table (ms):**

| N Products | Run 1 | Run 2 | Run 3 | Average |
|------------:|-------:|-------:|-------:|-------:|
| 1,000       | 62     | 90     | 59     | 70.33  |
| 2,000       | 67     | 56     | 73     | 65.33  |
| 4,000       | 52     | 105    | 87     | 81.33  |
| 8,000       | 110    | 87     | 89     | 95.33  |
| 16,000      | 93     | 152    | 121    | 122    |
| 32,000      | 134    | 123    | 208    | 155    |
| 64,000      | 191    | 166    | 190    | 182.33 |
| 100,000     | 257    | 299    | 357    | 304.33 |

---

### Synchronous Version
In the synchronous version, the conveyor is treated as a **single critical section**, so only one thread can access it at a time.  

- Results show **higher times and greater variability** as the number of products increases.  
- Although for 1,000 products the average was **74.33 ms**, similar to the asynchronous version, starting from **32,000 products** times increase drastically, reaching **5,177.33 ms**, and up to **12,798.33 ms** for **100,000 products**.  
- This demonstrates how **strict mutual exclusion over the entire conveyor** creates bottlenecks and limits thread concurrency.  

**Results table (ms):**

| N Products | Run 1 | Run 2 | Run 3 | Average |
|------------:|-------:|-------:|-------:|-------:|
| 1,000       | 53     | 104    | 66     | 74.33  |
| 2,000       | 78     | 211    | 91     | 126.67 |
| 4,000       | 86     | 215    | 141    | 147.33 |
| 8,000       | 177    | 162    | 146    | 161.67 |
| 16,000      | 227    | 151    | 432    | 270    |
| 32,000      | 3,772  | 5,263  | 6,497  | 5,177.33 |
| 64,000      | 11,863 | 5,372  | 6,430  | 7,888.33 |
| 100,000     | 16,457 | 10,292 | 11,646 | 12,798.33 |

---

### ⚖️ Performance Comparison
| Version        | Average Time (100,000 products) | Observations                                        |
|----------------|--------------------------------|---------------------------------------------------|
| Asynchronous   | 304.33 ms                       | Efficient concurrent access, stable times        |
| Synchronous    | 12,798.33 ms                     | Exclusive access, bottlenecks, high variability |

**💡 Conclusion:**  
The analysis shows that **per-position concurrent access** allows better performance and stability under heavy loads, while **strict mutual exclusion** over the entire conveyor ensures consistency at the expense of efficiency.  
This highlights the importance of balancing **concurrency** and **shared resource control** in multithreaded systems.
