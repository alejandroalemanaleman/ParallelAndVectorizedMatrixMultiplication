library(readxl)
library(ggplot2)
library(dplyr)
library(readxl)
library(ggplot2)
library(dplyr)
library(tidyr)

basic <- read_excel("/Users/alejandroalemanaleman/Downloads/bigdata3_individual/basic/results.xlsx") %>%
  as_tibble()
vectorized <- read_excel("/Users/alejandroalemanaleman/Downloads/bigdata3_individual/vectorized/MatrixMultiplicationBenchmarking.xlsx") %>%
  as_tibble()
atomic <- read_excel("/Users/alejandroalemanaleman/Downloads/bigdata3_individual/atomic/Matrix_Multiplication_Benchmark_Atomic.xlsx") %>%
  as_tibble()

data1 <- basic
data2 <- basic

data1$vectorized <- vectorized$execution_time_vectorized
data2$vectorized <- vectorized$memory_used_vectorized
data1$atomic <- atomic$`Execution Time (ms/op)`
data2$atomic <- atomic$`Memory Used (KB)`

data1 <- data1 %>% rename(basic = execution_time) 
data2 <- data2 %>% rename(basic = memory_used)
data1 <- data1 %>% select(-memory_used)
data2 <- data2 %>% select(-execution_time)




data1_<- data1 %>%
  pivot_longer(c(`basic`, `vectorized`, atomic), names_to = "algorithm", values_to = "execution_time")
d <- data1_ %>%
  filter(algorithm!="atomic")
ggplot(data1_, aes(x = n, y = execution_time, color = as.factor(algorithm))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(data1_$n)) + 
  labs(title = "Execution time by Matrix Size",
       x = "Matrix size",
       y = "Execution time (ms)",
       color = "") +
  theme_minimal() +
  #scale_y_log10() +
  theme(axis.text.y = element_text(color = "grey"))


basic <- basic %>%
  rename(`Matrix Size`=n)

atomic <- atomic %>%
  rename(`Matrix Size` = `Matrix Size (n)`, exec_atomic = `Execution Time (ms/op)`, mem_atomic = `Memory Used (KB)`)
atomic <- atomic %>%
  left_join(basic, by='Matrix Size') %>%
  rename(exec_basic=execution_time, mem_basic = memory_used)
atomic_ <- atomic %>%
  pivot_longer(c(exec_atomic, exec_basic), names_to = "algorithm", values_to = "execution_time")
atomic__ <- atomic %>%
  pivot_longer(c(mem_atomic, mem_basic), names_to = "algorithm", values_to = "memory_use")


ggplot(atomic__, aes(x = `Matrix Size`, y = memory_use, color = as.factor(algorithm))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(data1_$n)) + 
  labs(title = "Use of Memory by Matrix Size Naive vs Atomic",
       x = "Matrix size",
       y = "Use of Memory (KB)",
       color = "") +
  theme_minimal() +
  #scale_y_log10() +
  theme(axis.text.y = element_text(color = "grey"))
ggplot(atomic_, aes(x = `Matrix Size`, y = execution_time, color = as.factor(algorithm))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(data1_$n)) + 
  labs(title = "Execution time by Matrix Size Naive vs Atomic",
       x = "Matrix size",
       y = "Execution time (ms)",
       color = "") +
  theme_minimal() +
  #scale_y_log10() +
  theme(axis.text.y = element_text(color = "grey"))

vectorized_ <- vectorized%>%
  pivot_longer(c(execution_time_basic, execution_time_vectorized), names_to = "algorithm", values_to = "execution_time")


vectorized__ <- vectorized%>%
  pivot_longer(c(memory_used_basic, memory_used_vectorized), names_to = "algorithm", values_to = "memory_use")

ggplot(vectorized_, aes(x = `matriz_size`, y = execution_time, color = as.factor(algorithm))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(data1_$n)) + 
  labs(title = "Execution time by Matrix Size Naive vs Vectorized",
       x = "Matrix size",
       y = "Execution time (ms)",
       color = "") +
  theme_minimal() +
  #scale_y_log10() +
  theme(axis.text.y = element_text(color = "grey"))

ggplot(vectorized__, aes(x = `matriz_size`, y = memory_use, color = as.factor(algorithm))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(data1_$n)) + 
  labs(title = "Use of Memory by Matrix Size Naive vs Vectorized",
       x = "Matrix size",
       y = "Use of Memory (KB)",
       color = "") +
  theme_minimal() +
  #scale_y_log10() +
  theme(axis.text.y = element_text(color = "grey"))

