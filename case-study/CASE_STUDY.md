# Case Study Scenarios to discuss

## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.

**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations

**Questions you may have and considerations:**
- What is the cost accounting model (standard cost, actual cost, activity-based costing)? Allocation rules differ materially.
- Which dimensions must costs roll up to: business unit, location, store, SKU, time period?
- How do we handle shared costs (regional transport, HQ overhead)—fixed splits vs dynamic drivers (pallet moves, pick lines)?
- Is data sourced in real time from WMS/ERP or batch? Latency affects operational decisions.
- Experience: In distributed ops, the hardest part is often consistent master data (same warehouse code in finance and fulfillment) and reconciling timing (goods received vs invoiced).
- Key questions for stakeholders: required granularity for P&L, audit requirements, and whether stores are profit centers or cost centers.

## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.

**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?

**Questions you may have and considerations:**
- Strategies: consolidate slow movers, rebalance stock across warehouses, right-size warehouse capacity vs contract minimums, route optimization, labor scheduling, reduce shrink and damage.
- Identify how much it costs to deliver each product to each store from each warehouse. Break down the major expenses, such as transportation and employee costs, and find where most of the money is being spent. Also consider the extra costs caused by late deliveries or missed delivery deadlines, rather than looking only at the cost of delivering each unit.
- Prioritize tasks based on how much benefit they will bring, how much time and effort they require, and the risks involved. For example, closing a warehouse or storage location might save money on rent, but it could also increase delivery times and affect customer satisfaction. So, consider both cost savings and the impact on service before making a decision.
- Implement changes step by step instead of changing everything at once. Track important performance measures such as delivery cost per order, product availability (fill rate), and on-time delivery to ensure service quality doesn't decline. First, test the changes in a few regions, measure the results, and fix any issues before applying the same approach across all regions.
- Questions: minimum service levels contractually promised, union/labor constraints, and capex budget for automation.

## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.

**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have and how would you ensure seamless integration and data synchronization?

**Questions you may have and considerations:**
- Benefits: single version of truth for actuals vs operational metrics, faster month-end close, fewer manual spreadsheets, better variance analysis (budget vs forecast vs actual).
- Integration patterns: event-driven cost postings on operational milestones (shipment dispatched, warehouse day closed) vs nightly batch journals.
- Ensure quality with reconciliation jobs (operational totals vs GL), dead-letter queues, and clear ownership when systems disagree.
- Questions: which ERP/GL is system of record, required audit trail, and regulatory retention for cost history.

## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.

**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?

**Questions you may have and considerations:**
- Drivers: volume forecasts (orders, SKU mix), seasonality, headcount plans, fuel indices, rent escalations, new warehouse ramp curves.
- Design: scenario planning (base/upside/downside), rolling forecasts vs annual budget, versioned assumptions stored with who changed what.
- Tie operational master data (locations, capacity limits like in this domain) to financial capacity planning—cannot budget storage without knowing colocation caps.
- Feedback loop: compare forecast error by category monthly and auto-suggest revised drivers.
- Questions: horizon (12 vs 24 months), granularity, and integration with workforce and procurement planning.

## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.

**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?

**Questions you may have and considerations:**
- Preserving history under the same business unit code maintains continuity for YoY comparisons, contracts tied to BU, and audit trails while the physical site changes.
- Archive operational entity but retain immutable cost ledger lines keyed by BU + warehouse instance id + date range.
- Budget for the new site should separate one-time transition costs (moving stock, downtime, dual-running) from run-rate opex; baseline run-rate from historical actuals adjusted for capacity/stock changes.
- Relates directly to this codebase: replace archives old warehouse and creates new active row with same BU—financial tooling should mirror that with explicit effective-dated cost records.
- Questions: transfer pricing for stock moved, how long parallel operations run, and who approves budget transfer from old to new cost center.

## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.

**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.
