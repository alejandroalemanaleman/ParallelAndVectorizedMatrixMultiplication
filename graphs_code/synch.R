library(readxl)
library(ggplot2)
library(dplyr)
library(readxl)
library(ggplot2)
library(dplyr)
library(tidyr)


semaphore <- read_excel("/Users/alejandroalemanaleman/Downloads/bigdata3_individual/semaphore/semaphore_results.xlsx") %>%
  as_tibble()
semaphore_ <- semaphore %>% mutate(speed_up_semaphore = exec_basic/exec_semaphore) 
semaphore_ <- semaphore_%>% mutate(efficiency_semaphore = speed_up_semaphore/`Number of Threads`) %>% filter(`Number of Threads`!=32) %>%
  rename(Threads = `Number of Threads`) %>% 
  rename(`Matrix Size` = `Matrix Size (n)`)

ggplot(semaphore_, aes(x = `Number of Threads`, y = `speed_up`, color = as.factor( `Matrix Size (n)`))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(fixed_$`Number of Threads`)) + 
  labs(title = "Speed Up by Number of Threads and Matrix Size in Fixed Thread Pool",
       x = "Number of threads",
       y = "Speed Up",
       color = "") +
  theme_minimal() +
  theme(axis.text.y = element_text(color = "grey"))

ggplot(semaphore_, aes(x = `Number of Threads`, y = `efficiency`, color = as.factor( `Matrix Size (n)`))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(fixed_$`Number of Threads`)) + 
  labs(title = "Efficiency by Number of Threads and Matrix Size in Fixed Thread Pool",
       x = "Number of Threads",
       y = "Efficiency",
       color = "") +
  theme_minimal() +
  theme(axis.text.y = element_text(color = "grey"))





synchronized <- read_excel("/Users/alejandroalemanaleman/Downloads/bigdata3_individual/synchronized/synchronized.xlsx") %>%
  as_tibble()
synchronized_ <- synchronized %>% mutate(speed_up_synch = exec_basic/exec_synchronized) 
synchronized_ <- synchronized_%>% mutate(efficiency_synch = speed_up_synch/`Threads`) %>% filter(`Threads`!=32)

ggplot(synchronized_, aes(x = `Threads`, y = `speed_up`, color = as.factor( `Matrix Size`))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(fixed_$`Number of Threads`)) + 
  labs(title = "Speed Up by Number of Threads and Matrix Size in Fixed Thread Pool",
       x = "Number of threads",
       y = "Speed Up",
       color = "") +
  theme_minimal() +
  theme(axis.text.y = element_text(color = "grey"))



ggplot(synchronized_, aes(x = `Threads`, y = `efficiency`, color = as.factor( `Matrix Size`))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(fixed_$`Number of Threads`)) + 
  labs(title = "Efficiency by Number of Threads and Matrix Size in Fixed Thread Pool",
       x = "Number of Threads",
       y = "Efficiency",
       color = "") +
  theme_minimal() +
  theme(axis.text.y = element_text(color = "grey"))





synchronized_<- synchronized_ %>%
  rename( mem_synchronized = `Memory Usage (KB)`)

streams_<- streams_ %>%
  rename(speed_up_streams = speed_up, efficiency_streams = efficiency, `Matrix Size`=`Matrix Size (n)`)

fixed_<- fixed_ %>%
  rename(speed_up_fixed = speed_up, efficiency_fixed = efficiency, `Matrix Size`=`Matrix Size (n)`, Threads = `Number of Threads`, mem_fixed = `Memory Usage (KB)`)

parallel <- semaphore_ %>% 
  rename(mem_semaphore=`Memory Used (KB)`) %>%
  left_join(synchronized_,by = c("Matrix Size", "Threads"))

parallel <- parallel %>%  
  left_join(streams_,by = c("Matrix Size", "Threads")) %>%
  left_join(fixed_ ,by = c("Matrix Size", "Threads"))

parallel_ <- parallel %>%
  pivot_longer(c(speed_up_semaphore, speed_up_synch, speed_up_streams, speed_up_fixed), names_to = "algorithm", values_to = "speed_up")
parallel__ <- parallel %>%
  pivot_longer(c(efficiency_semaphore, efficiency_synch, efficiency_streams, efficiency_fixed), names_to = "algorithm", values_to = "efficiency")
parallel___ <- parallel %>%
  pivot_longer(c(mem_semaphore, mem_synchronized, mem_streams, mem_fixed, mem_basic), names_to = "algorithm", values_to = "memory_use")

basic <- read_excel("/Users/alejandroalemanaleman/Downloads/bigdata3_individual/basic/results.xlsx") %>%
  as_tibble() %>%
  rename(mem_basic = memory_used, `Matrix Size`=n)

a <-parallel %>%
  left_join(basic, by = "Matrix Size")


ggplot(parallel_, aes(x = `Threads`, y = `speed_up`, color = as.factor( `algorithm`))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(fixed_$`Number of Threads`)) + 
  labs(title = "Speed Up by Number of Threads and Matrix Size in in different Parallel algorithms",
       x = "Number of Threads",
       y = "Speed Up",
       color = "") +
  theme_minimal() +
  facet_wrap(~ `Matrix Size`, ncol = 2) 
  theme(axis.text.y = element_text(color = "grey"))

  
  ggplot(parallel__, aes(x = `Threads`, y = `efficiency`, color = as.factor( `algorithm`))) +
    geom_line() +
    geom_point() +
    #scale_x_continuous(breaks = 1:max(fixed_$`Number of Threads`)) + 
    labs(title = "Efficiency by Number of Threads and Matrix Size in in different Parallel algorithms",
         x = "Number of Threads",
         y = "Efficiency",
         color = "") +
    theme_minimal() +
    facet_wrap(~ `Matrix Size`, ncol = 2) 
  theme(axis.text.y = element_text(color = "grey"))

  parallel___ <- a %>%
    pivot_longer(c(mem_semaphore, mem_synchronized, mem_streams, mem_fixed, mem_basic), names_to = "algorithm", values_to = "memory_use")
  parallel____ <- a %>%
    pivot_longer(c(exec_semaphore, exec_synchronized, exec_streams, exec_fixed, exec_basic.x), names_to = "algorithm", values_to = "execution__time")
  
  
  ggplot(parallel____, aes(x = `Matrix Size`, y = `execution__time`, color = as.factor( `algorithm`))) +
    geom_line() +
    geom_point() +
    #scale_x_continuous(breaks = 1:max(fixed_$`Number of Threads`)) + 
    labs(title = "Execution Time by Matrix Size and Number of Threads in different Parallel algorithms and Naive.",
         x = "Matrix Size",
         y = "Execution Time (ms)",
         color = "") +
    theme_minimal() +
    facet_wrap(~ `Threads`, ncol = 2) 
  theme(axis.text.y = element_text(color = "grey"))
  
  ggplot(parallel___, aes(x = `Matrix Size`, y = `memory_use`, color = as.factor( `algorithm`))) +
    geom_line() +
    geom_point() +
    #scale_x_continuous(breaks = 1:max(fixed_$`Number of Threads`)) + 
    labs(title = "Use of Memory by Matrix Size and Number of Threads in different Parallel algorithms and Naive.",
         x = "Matrix Size",
         y = "Use of Memory (KB)",
         color = "") +
    theme_minimal() +
    facet_wrap(~ `Threads`, ncol = 2) 
  theme(axis.text.y = element_text(color = "grey"))