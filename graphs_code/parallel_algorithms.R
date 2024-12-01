library(readxl)
library(ggplot2)
library(dplyr)
library(readxl)
library(ggplot2)
library(dplyr)
library(tidyr)


# FIXED
fixed <- read_excel("/Users/alejandroalemanaleman/Downloads/bigdata3_individual/fixed/results2.xlsx") %>%
  as_tibble()
fixed_ <- fixed %>% mutate(speed_up = exec_basic/exec_fixed) 
fixed_ <- fixed_%>% mutate(efficiency = speed_up/`Number of Threads`)

ggplot(fixed_, aes(x = `Number of Threads`, y = `speed_up`, color = as.factor( `Matrix Size (n)`))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(fixed_$`Number of Threads`)) + 
  labs(title = "Speed Up by Number of Threads and Matrix Size in Fixed Thread Pool",
       x = "Number of threads",
       y = "Speed Up",
       color = "") +
  theme_minimal() +
  theme(axis.text.y = element_text(color = "grey"))

  ggplot(fixed_, aes(x = `Number of Threads`, y = `efficiency`, color = as.factor( `Matrix Size (n)`))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(fixed_$`Number of Threads`)) + 
  labs(title = "Efficiency by Number of Threads and Matrix Size in Fixed Thread Pool",
       x = "Number of Threads",
       y = "Efficiency",
       color = "") +
  theme_minimal() +
  theme(axis.text.y = element_text(color = "grey"))

# STREAMS
streams <- read_excel("/Users/alejandroalemanaleman/Downloads/bigdata3_individual/parallel_streams/benchmark_parallel_streams.xlsx") %>%
    as_tibble()
streams_ <- streams %>% mutate(speed_up = exec_basic/exec_streams) 
streams_ <- streams_%>% mutate(efficiency = speed_up/`Threads`)

ggplot(streams_, aes(x = `Threads`, y = `speed_up`, color = as.factor( `Matrix Size (n)`))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(streams_$`Threads`)) + 
  labs(title = "Speed Up by Number of Threads and Matrix Size in Parallel Streams",
       x = "Number of threads",
       y = "Speed Up",
       color = "") +
  theme_minimal() +
  theme(axis.text.y = element_text(color = "grey"))

ggplot(streams_, aes(x = `Threads`, y = efficiency, color = as.factor( `Matrix Size (n)`))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(streams_$`Threads`)) + 
  labs(title = "Efficiency by Number of Threads and Matrix Size in Parallel Streams",
       x = "Number of threads",
       y = "Efficiency",
       color = "") +
  theme_minimal() +
  theme(axis.text.y = element_text(color = "grey"))

exec <- streams_
exec$exec_fixed <- fixed_$exec_fixed

exec2 <- exec %>%
  filter(`Matrix Size (n)`%in% c(1000))%>%
  pivot_longer(c(`exec_basic`, `exec_fixed`, exec_streams), names_to = "algorithm", values_to = "execution_time")



ggplot(exec2, aes(x = `Threads`, y = execution_time, color = as.factor( `algorithm`))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(streams_$`Threads`)) + 
  labs(title = "Average execution time of Parallel algorithms vs Basic algorithm \nvarying matrix size",
       x = "Matrix Size",
       y = "Execution time (ms) ",
       color = "") +
  theme_minimal() +
  theme(axis.text.y = element_text(color = "grey"))




exec_summary <- exec %>%
  group_by(`Matrix Size (n)`) %>%   # Agrupamos por Threads
  summarize(mean_basic = mean(exec_basic), mean_fixed = mean(exec_fixed),mean_streams = mean(exec_streams))%>%
  pivot_longer(c(`mean_basic`, `mean_fixed`, mean_streams), names_to = "algorithm", values_to = "execution_time")

exec_summary <- exec %>%
# filter(Threads==8)%>%
  pivot_longer(c(`exec_basic`, `exec_fixed`, exec_streams), names_to = "algorithm", values_to = "execution_time")

ggplot(exec_summary, aes(x = `Matrix Size (n)`, y = execution_time, color = as.factor( `algorithm`))) +
  geom_line() +
  geom_point() +
  #scale_x_continuous(breaks = 1:max(streams_$`Threads`)) + 
  labs(title = "Execution time of Basic algorithm vs Parallel algorithms\nby Matrix size varying number of Threads",
       x = "Matrix Size",
       y = "Execution time (ms) ",
       color = "") +
  theme_minimal() +
  facet_wrap(~ Threads, ncol = 2) +  # Divide los gráficos por `Threads`
  theme(axis.text.y = element_text(color = "grey"))

